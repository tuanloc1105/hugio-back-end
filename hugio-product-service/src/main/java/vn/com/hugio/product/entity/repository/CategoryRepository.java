package vn.com.hugio.product.entity.repository;

import vn.com.hugio.common.entity.repository.BaseRepository;
import vn.com.hugio.product.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends BaseRepository<Category> {

    Optional<Category> findByCategoryName(String name);

    Optional<Category> findByCategoryUid(String categoryUid);

}
