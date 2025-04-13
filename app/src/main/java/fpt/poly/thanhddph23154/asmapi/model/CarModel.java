package fpt.poly.thanhddph23154.asmapi.model;

import com.google.gson.annotations.SerializedName;

public class CarModel {

    private String _id;
    private String ten;

    @SerializedName("namsx")

    private String namsx;

    private String xuatxu;

    private double gia;

    private String img;

    private boolean liked;

    private String mota;

    private int soluong;

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }


    public CarModel(String mota) {
        this.mota = mota;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public boolean isLiked() { return liked; }
    public void setLiked(boolean liked) { this.liked = liked; }


    public CarModel() {
    }

    public CarModel(String _id, String ten, String namsx, String xuatxu, double gia, String img) {
        this._id = _id;
        this.ten = ten;
        this.namsx = namsx;
        this.xuatxu = xuatxu;
        this.gia = gia;
        this.img = img;
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

    public String getNamsx() {
        return namsx;
    }

    public void setNamsx(String namsx) {
        this.namsx = namsx;
    }

    public String getXuatxu() {
        return xuatxu;
    }

    public void setXuatxu(String xuatxu) {
        this.xuatxu = xuatxu;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
