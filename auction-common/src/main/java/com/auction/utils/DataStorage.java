package com.auction.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataStorage {

    public static <T> boolean save(String filePath, T data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
            oos.flush();
            System.out.println("[DataStorage] Đã lưu dữ liệu vào " + filePath);
            return true;
        } catch (IOException e) {
            System.err.println("[DataStorage] Lỗi khi lưu dữ liệu: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    @SuppressWarnings("unchecked")
    public static <T> T load(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("[DataStorage] File " + filePath + " chưa tồn tại. Bỏ qua nạp dữ liệu.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[DataStorage] Lỗi khi nạp dữ liệu: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}