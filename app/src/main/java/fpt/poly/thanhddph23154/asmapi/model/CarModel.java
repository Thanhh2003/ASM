package fpt.poly.thanhddph23154.asmapi.model;

import com.google.gson.annotations.SerializedName;

public class CarModel {

    private String _id;
    private String ten;

    @SerializedName("namsx")

    private int namsx;

    private String hang;

    private double gia;


    public CarModel() {
    }

    public CarModel(String _id, String ten, int namsx, String hang, double gia) {
        this._id = _id;
        this.ten = ten;
        this.namsx = namsx;
        this.hang = hang;
        this.gia = gia;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getNamsx() {
        return namsx;
    }

    public void setNamsx(int namsx) {
        this.namsx = namsx;
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }
}
