package vn.com.hugio.inventory.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.service.BaseService;
import vn.com.hugio.inventory.entity.InventoryLog;
import vn.com.hugio.inventory.entity.ProductInventory;
import vn.com.hugio.inventory.entity.repository.ProductInventoryRepository;
import vn.com.hugio.inventory.enums.ImportBehaviour;
import vn.com.hugio.inventory.request.InventoryRequest;
import vn.com.hugio.inventory.service.InventoryLogService;
import vn.com.hugio.inventory.service.ProductInventoryService;

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
        inventoryLogService.create(inventoryLog);
    }

    @Override
    public void add(InventoryRequest request) {
        ProductInventory productInventory = this.repository.findByProductUid(request.getProductUid()).orElseThrow(() -> new InternalServiceException(ErrorCodeEnum.NOT_EXISTS));

        this.save(productInventory);
        InventoryLog inventoryLog = this.modelMapper.map(request, InventoryLog.class);
        inventoryLog.setBehaviour(ImportBehaviour.CREATE);
        inventoryLogService.create(inventoryLog);
    }
}
