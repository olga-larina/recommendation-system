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

import java.util.ArrayList;
import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    private final List<Manufacturer> manufacturers = new ArrayList<>();
    private final List<Category> categories = new ArrayList<>();
    private final List<Product> products = new ArrayList<>();

    @ChangeSet(order = "001", id = "dropDb", author = "olga", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "initManufacturers", author = "olga")
    public void initManufacturers(ManufacturerDao manufacturerDao) {
        manufacturers.add(manufacturerDao.save(new Manufacturer("Purina Pro Plan")));
        manufacturers.add(manufacturerDao.save(new Manufacturer("Hills")));
        manufacturers.add(manufacturerDao.save(new Manufacturer("Royal Canin")));
        manufacturers.add(manufacturerDao.save(new Manufacturer("Зооник")));
        manufacturers.add(manufacturerDao.save(new Manufacturer("Foxie")));
        manufacturers.add(manufacturerDao.save(new Manufacturer("Petgeek")));
    }

    @ChangeSet(order = "003", id = "initCategories", author = "olga")
    public void initCategories(CategoryDao categoryDao) {
        categories.add(categoryDao.save(new Category("Корма")));
        categories.add(categoryDao.save(new Category("Наполнители")));
        categories.add(categoryDao.save(new Category("Когтеточки")));
        categories.add(categoryDao.save(new Category("Игрушки")));
        categories.add(categoryDao.save(new Category("Товары для кошек")));
        categories.add(categoryDao.save(new Category("Товары для собак")));
    }

    @ChangeSet(order = "004", id = "initProducts", author = "olga")
    public void initProducts(ProductDao productDao) {
        products.add(productDao.save(new Product("Влажный корм для котят с тунцом", "100", manufacturers.get(0), List.of(categories.get(0), categories.get(4)))));
        products.add(productDao.save(new Product("Влажный корм для взрослых собак с говядиной", "150", manufacturers.get(0), List.of(categories.get(0), categories.get(5)))));
        products.add(productDao.save(new Product("Сухой корм для кошек для шерсти", "200", manufacturers.get(1), List.of(categories.get(0), categories.get(4)))));
        products.add(productDao.save(new Product("Сухой корм для собак при аллергии", "250", manufacturers.get(1), List.of(categories.get(0), categories.get(5)))));
        products.add(productDao.save(new Product("Лакомство для кошек с уткой", "300", manufacturers.get(2), List.of(categories.get(0), categories.get(4)))));
        products.add(productDao.save(new Product("Лакомство для собак с курицей", "350", manufacturers.get(2), List.of(categories.get(0), categories.get(5)))));
        products.add(productDao.save(new Product("Наполнитель комкующийся", "400", manufacturers.get(3), List.of(categories.get(1), categories.get(4)))));
        products.add(productDao.save(new Product("Наполнитель древесный", "450", manufacturers.get(3), List.of(categories.get(1), categories.get(4)))));
        products.add(productDao.save(new Product("Когтеточка мини", "500", manufacturers.get(4), List.of(categories.get(2), categories.get(4)))));
        products.add(productDao.save(new Product("Когтеточка домик", "550", manufacturers.get(4), List.of(categories.get(2), categories.get(4)))));
        products.add(productDao.save(new Product("Интерактивная мышка", "600", manufacturers.get(5), List.of(categories.get(3), categories.get(4)))));
        products.add(productDao.save(new Product("Мячик", "650", manufacturers.get(5), List.of(categories.get(3), categories.get(5)))));
    }
}