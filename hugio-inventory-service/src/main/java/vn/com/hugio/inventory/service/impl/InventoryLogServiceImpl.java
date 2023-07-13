package vn.com.hugio.inventory.service.impl;

import org.springframework.stereotype.Service;
import vn.com.hugio.common.service.BaseService;
import vn.com.hugio.inventory.entity.InventoryLog;
import vn.com.hugio.inventory.entity.repository.InventoryLogRepository;
import vn.com.hugio.inventory.service.InventoryLogService;

@Service
public class InventoryLogServiceImpl extends BaseService<InventoryLog, InventoryLogRepository> implements InventoryLogService {
    public InventoryLogServiceImpl(InventoryLogRepository repository) {
        super(repository);
    }

    @Override
    public void saveEntity(InventoryLog inventoryLog) {
        this.save(inventoryLog);
    }
}
