package vn.com.hugio.inventory.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.common.service.BaseService;
import vn.com.hugio.inventory.dto.ProductQuantityDto;
import vn.com.hugio.inventory.entity.InventoryLog;
import vn.com.hugio.inventory.entity.ProductInventory;
import vn.com.hugio.inventory.entity.repository.ProductInventoryRepository;
import vn.com.hugio.inventory.enums.ImportBehaviour;
import vn.com.hugio.inventory.request.InventoryRequest;
import vn.com.hugio.inventory.request.ReduceProductQuantityRequest;
import vn.com.hugio.inventory.service.InventoryLogService;
import vn.com.hugio.inventory.service.ProductInventoryService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = {Exception.class, Throwable.class, RuntimeException.class, Error.class})
public class ProductInventoryServiceImpl extends BaseService<ProductInventory, ProductInventoryRepository> implements ProductInventoryService {

    private final InventoryLogService inventoryLogService;
    private final ModelMapper modelMapper;

    public ProductInventoryServiceImpl(ProductInventoryRepository repository,
                                       InventoryLogService inventoryLogService,
                                       ModelMapper modelMapper) {
        super(repository);
        this.inventoryLogService = inventoryLogService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(InventoryRequest request) {
        Optional<ProductInventory> optionalProductInventory = this.repository.findByProductUid(request.getProductUid());
        if (optionalProductInventory.isPresent()) {
            throw new InternalServiceException(ErrorCodeEnum.EXISTS);
        }
        this.save(
                ProductInventory.builder()
                        .productUid(request.getProductUid())
                        .quantity(request.getImportedQuantity())
                        .build()
        );
        InventoryLog inventoryLog = this.modelMapper.map(request, InventoryLog.class);
        inventoryLog.setBehaviour(ImportBehaviour.CREATE);
        inventoryLogService.saveEntity(inventoryLog);
    }

    @Override
    public void importProduct(InventoryRequest request) {
        ProductInventory productInventory = this.repository.findByProductUid(request.getProductUid()).orElseThrow(() -> new InternalServiceException(ErrorCodeEnum.NOT_EXISTS));
        Long currentQuantity = productInventory.getQuantity();
        productInventory.setQuantity(currentQuantity + request.getImportedQuantity());
        this.save(productInventory);
        InventoryLog inventoryLog = this.modelMapper.map(request, InventoryLog.class);
        inventoryLog.setBehaviour(ImportBehaviour.IMPORT);
        inventoryLogService.saveEntity(inventoryLog);
    }

    @Override
    public void updateProduct(InventoryRequest request) {
        ProductInventory productInventory = this.repository.findByProductUid(request.getProductUid()).orElseThrow(() -> new InternalServiceException(ErrorCodeEnum.NOT_EXISTS));
        productInventory.setQuantity(request.getImportedQuantity());
        this.save(productInventory);
        InventoryLog inventoryLog = this.modelMapper.map(request, InventoryLog.class);
        inventoryLog.setBehaviour(ImportBehaviour.IMPORT);
        inventoryLogService.saveEntity(inventoryLog);
    }

    @Override
    public ProductQuantityDto getProductQuantity(InventoryRequest request) {
        return this.repository.findQuantityInfo(request.getProductUid());
    }

    @Override
    public void reduceProductQuantity(List<ReduceProductQuantityRequest> request) {
        for (ReduceProductQuantityRequest rq : request) {
            Optional<ProductInventory> optionalProductInventory = this.repository.findByProductUid(rq.getProductUid());
            if (optionalProductInventory.isEmpty()) {
                LOG.info("product with uid %s is not present", rq.getProductUid());
                continue;
            }
            ProductInventory productInventory = optionalProductInventory.get();
            long newQuantity = productInventory.getQuantity() - rq.getQuantity();
            if (newQuantity < 0L) {
                throw new InternalServiceException(ErrorCodeEnum.FAILURE, "product out of stock: " + rq.getProductUid());
            }
            productInventory.setQuantity(newQuantity);
        }
    }
}
