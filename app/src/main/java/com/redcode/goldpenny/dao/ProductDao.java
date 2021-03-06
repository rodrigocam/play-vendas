package com.redcode.goldpenny.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.redcode.goldpenny.model.Product;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ProductDao {
    @Insert(onConflict = REPLACE)
    void save(Product product);

    @Query("SELECT * FROM product WHERE id = :productId")
    LiveData<Product> load(int productId);

    @Query("SELECT * FROM product")
    LiveData<List<Product>> loadAll();

    @Query("SELECT COUNT(*) from product")
    int getProductCount();

    @Query("DELETE FROM product")
    public void deleteProducts();
}