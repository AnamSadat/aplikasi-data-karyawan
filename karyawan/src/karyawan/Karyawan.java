package karyawan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Hp
 */
public class Karyawan {
    private String dbName;
    private String dbUser;
    private String dbPassword;
    private String dbUrl;
    private String tableName;
    Connection connect;
    
    private String id;
    private String nama;
    private String jabatan;
    private int gaji;
    
    public Karyawan(String url, String name, String user, String password) {
        this.dbName = name;
        this.dbUser = user;
        this.dbPassword = password;
        this.dbUrl = url;
    }
    
    public void setDataKaryawan(String id, String nama, String jabatan, int gaji) {
        this.id = id;
        this.nama = nama;
        this.jabatan = jabatan;
        this.gaji = gaji;
    }
    
    public void setTableName(String tableName){
        this.tableName = tableName;
    }
    
    public void setId(String id){
        this.id = id;
    }
    
    public Connection createConnection() {
        String mysqlUrl = this.dbUrl + this.dbName +"?user="+ this.dbUser +"&password="+ this.dbPassword;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection(mysqlUrl);
        }catch(ClassNotFoundException error) {
            System.out.println("Driver MySql tidak ditemukan!\n" + error);
        }catch(SQLException error){
            System.out.println("Internal Server Error\n" + error);
        }
        
        return connect;
    }
    
    public List<String> getKaryawan() {
        List<String> data = new ArrayList<>();
        
        try {
            createConnection();
            String sql = "SELECT * FROM " + this.tableName;
            ResultSet result = connect.createStatement().executeQuery(sql);
            
            while (result.next()){
                String id = result.getString("id");
                String nama = result.getString("nama");
                String jabatan = result.getString("jabatan");
                int gaji = result.getInt("gaji");
                
                String baris = id + " - " + nama + " - " + jabatan + " - " + gaji;
                data.add(baris);
            }
        }catch(SQLException error){
            String message = "Gagal mengambil data: " + error.getMessage();
            System.out.println(message);
        }
        
        return data;
    }
    
    public String addKaryawan() {
        try {
            createConnection();

            String sql = "INSERT INTO " + this.tableName + " (id, nama, jabatan, gaji) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = connect.prepareStatement(sql);

            ps.setString(1, this.id);
            ps.setString(2, this.nama);
            ps.setString(3, this.jabatan);
            ps.setDouble(4, this.gaji);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                return "Karyawan berhasil ditambahkan!";
            }

        } catch (SQLException error) {
            return "Gagal menambahkan karyawan: " + error.getMessage();
        }

        return "Tidak ada data yang ditambahkan.";
    }
    
    public String deleteKaryawan() {
        try {
            createConnection();

            String sql = "DELETE FROM " + this.tableName + " WHERE id = ?";
            PreparedStatement ps = connect.prepareStatement(sql);

            ps.setString(1, this.id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                return "Karyawan dengan ID " + this.id + " berhasil dihapus!";
            } else {
                return "Karyawan dengan ID " + this.id + " tidak ditemukan.";
            }

        } catch (SQLException error) {
            return "Gagal menghapus karyawan: " + error.getMessage();
        }
    }
}
