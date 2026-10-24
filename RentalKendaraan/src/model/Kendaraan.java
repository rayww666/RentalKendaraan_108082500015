package model;

public abstract class Kendaraan {
    private final String platNomor;
    private final String merk;
    private final long tarifPerHari;
    private boolean tersedia = true;

    public Kendaraan(String platNomor, String merk, long tarifPerHari) {
        this.platNomor = platNomor;
        this.merk = merk;
        this.tarifPerHari = tarifPerHari;
    }

    public abstract String getJenis();

    public abstract long hitungBiaya(int hari);

    public int getMinimalHari() {
        return 1;
    }

    public String getPlatNomor() {
        return platNomor;
    }

    public String getMerk() {
        return merk;
    }

    public long getTarifPerHari() {
        return tarifPerHari;
    }

    public boolean isTersedia() {
        return tersedia;
    }

    public void setTersedia(boolean tersedia) {
        this.tersedia = tersedia;
    }
}
