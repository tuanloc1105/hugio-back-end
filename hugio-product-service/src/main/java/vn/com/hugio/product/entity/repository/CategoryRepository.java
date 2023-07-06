package vn.com.hugio.product.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.com.hugio.common.entity.repository.BaseRepository;
import vn.com.hugio.product.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends BaseRepository<Category> {

    Optional<Category> findByCategoryName(String name);

    Optional<Category> findByCategoryUid(String categoryUid);

    List<Category> findByCategoryNameInOrCategoryUidIn(List<String> id, List<String> name);

    Page<Category> findByActiveIsTrue(Pageable pageable);

}
