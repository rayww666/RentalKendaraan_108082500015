package controller;

import model.Kendaraan;
import model.Penyewaan;
import model.KendaraanRepository;
import model.PenyewaanRepository;
import model.WajibJaminan;
import model.WajibSIM;
import view.RentalView;
import view.StrukView;
import model.KendaraanFactory;

public class RentalController {
    private final KendaraanRepository kendaraanRepo;
    private final PenyewaanRepository penyewaanRepo;
    private final RentalView view;
    private final StrukView struktView;
    private final KendaraanFactory kendaraanFactory;

    public RentalController(
            KendaraanRepository kendaraanRepository,
            PenyewaanRepository penyewaanRepository,
            RentalView view,
            StrukView strukView,
            KendaraanFactory kendaraanFactory) {
        this.kendaraanRepo = kendaraanRepository;
        this.penyewaanRepo = penyewaanRepository;
        this.view = view;
        this.struktView = strukView;
        this.kendaraanFactory = kendaraanFactory;
    }

    public void tambahDataAwal(Kendaraan kendaraan) {
        kendaraanRepo.tambah(kendaraan);
    }

    public void jalankan() {
        while (true) {
            String pilihan = view.tampilMenu();
            switch (pilihan) {
                case "1" -> view.tampilDaftarKendaraan(kendaraanRepo.semua());
                case "2" -> tambahKendaraan();
                case "3" -> sewaKendaraan();
                case "4" -> kembalikanKendaraan();
                case "5" -> view.tampilRiwayat(penyewaanRepo.semua());
                case "0" -> {
                    view.tampilPesan("Thank You.");
                    return;
                }
                default -> view.tampilPesan("Menu tidak ada.");
            }
        }
    }

    public void tambahKendaraan() {
        String plat = view.bacaTeks("Plat Nomor");
        if (plat.isEmpty() || kendaraanRepo.cariByPlat(plat) != null) {
            view.tampilPesan("Plat nomor kosong atau sudah terdaftar.");
            return;
        }
        String merk = view.bacaTeks("Merk");
        String jenis = view.bacaTeks("Jenis (" + String.join("/", kendaraanFactory.daftarJenis()) + ")").toUpperCase();

        if (!kendaraanFactory.daftarJenis().contains(jenis)) {
            view.tampilPesan("Jenis tidak dikenal.");
            return;
        }
        long tarif = view.bacaAngka("Tarif per hari");
        if (tarif <= 0) {
            view.tampilPesan("Tarif harus lebih dari 0.");
            return;
        }
        kendaraanRepo.tambah(kendaraanFactory.buat(jenis, plat, merk, tarif));
        view.tampilPesan("Kendaraan berhasil ditambahkan.");
    }

    public void sewaKendaraan() {
        String plat = view.bacaTeks("Plat Nomor");
        Kendaraan kendaraan = kendaraanRepo.cariByPlat(plat);
        if (kendaraan == null || !kendaraan.isTersedia()) {
            view.tampilPesan("Kendaraan tidak ditemukan atau sedang disewa.");
            return;
        }
        String nama = view.bacaTeks("Nama Penyewa");

        if (kendaraan instanceof WajibSIM wajibSIM) {
            String sim = view.bacaTeks("Golongan SIM Penyewa");
            if (!sim.equalsIgnoreCase(wajibSIM.getGolonganSIM())) {
                view.tampilPesan(
                        kendaraan.getMerk()
                                + " Wajib SIM "
                                + wajibSIM.getGolonganSIM() + ".");
                return;
            }
        }

        int hari = (int) view.bacaAngka("Lama sewa (hari)");

        if (kendaraan instanceof WajibJaminan wajibJaminan) {
            if (!view.konfirmasiUangJaminan(wajibJaminan.getUangJaminan())) {
                view.tampilPesan("Penyewaan dibatalkan.");
                return;
            }
        }

        if (hari < kendaraan.getMinimalHari()) {
            view.tampilPesan(kendaraan.getMerk() + " minimal disewa "
                    + kendaraan.getMinimalHari() + " hari.");
            return;
        }

        long total = kendaraan.hitungBiaya(hari);
        Penyewaan sewa = new Penyewaan(penyewaanRepo.buatIdBaru(), nama, kendaraan, hari, total);
        penyewaanRepo.tambah(sewa);
        kendaraan.setTersedia(false);
        struktView.cetak(sewa);
    }

    public void kembalikanKendaraan() {
        String plat = view.bacaTeks("Plat Nomor");
        Penyewaan sewa = penyewaanRepo.cariAktifByPlat(plat);
        if (sewa == null) {
            view.tampilPesan("Tidak ada penyewaan.");
            return;
        }
        sewa.selesai();
        sewa.getKendaraan().setTersedia(true);
        view.tampilPesan("Kendaraan " + sewa.getKendaraan().getMerk() + " telah dikembalikan.");
    }
}