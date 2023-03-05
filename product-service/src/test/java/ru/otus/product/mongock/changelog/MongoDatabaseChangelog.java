package ru.otus.product.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.product.dao.CategoryDao;
import ru.otus.product.dao.ManufacturerDao;
import ru.otus.product.dao.ProductDao;
import ru.otus.product.domain.Category;
import ru.otus.product.domain.Manufacturer;
import ru.otus.product.domain.Product;

import java.util.List;

@ChangeLog
public class MongoDatabaseChangelog {

    public static Manufacturer MANUFACTURER_1;
    public static Manufacturer MANUFACTURER_2;
    public static Category CATEGORY_1;
    public static Category CATEGORY_2;
    public static Category CATEGORY_3;
    public static Product PRODUCT_1;
    public static Product PRODUCT_2;
    public static Product PRODUCT_3;

    @ChangeSet(order = "001", id = "dropDb", author = "olga", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "initManufacturers", author = "olga")
    public void initManufacturers(ManufacturerDao manufacturerDao) {
        MANUFACTURER_1 = manufacturerDao.save(new Manufacturer("Purina Pro Plan"));
        MANUFACTURER_2 = manufacturerDao.save(new Manufacturer("Hills"));
    }

    @ChangeSet(order = "003", id = "initCategories", author = "olga")
    public void initCategories(CategoryDao categoryDao) {
        CATEGORY_1 = categoryDao.save(new Category("Корма"));
        CATEGORY_2 = categoryDao.save(new Category("Товары для кошек"));
        CATEGORY_3 = categoryDao.save(new Category("Товары для собак"));
    }

    @ChangeSet(order = "004", id = "initProducts", author = "olga")
    public void initProducts(ProductDao productDao) {
        PRODUCT_1 = productDao.save(new Product("Влажный корм для котят с тунцом", "100", MANUFACTURER_1, List.of(CATEGORY_1, CATEGORY_2)));
        PRODUCT_2 = productDao.save(new Product("Сухой корм для кошек для шерсти", "200", MANUFACTURER_2, List.of(CATEGORY_1, CATEGORY_2)));
        PRODUCT_3 = productDao.save(new Product("Лакомство для собак с курицей", "300", MANUFACTURER_2, List.of(CATEGORY_1, CATEGORY_3)));
    }
}