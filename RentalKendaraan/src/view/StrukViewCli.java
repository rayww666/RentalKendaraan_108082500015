package view;

import model.Penyewaan;

public class StrukViewCli implements StrukView {
    @Override
    public void cetak(Penyewaan p) {
        System.out.println("-------------- STRUK SEWA --------------");
        System.out.println("No. Sewa    : " + p.getId());
        System.out.println("Penyewa     : " + p.getNamaPenyewa());
        System.out.println("Kendaraan   : " + p.getKendaraan().getMerk() +
                " (" + p.getKendaraan().getPlatNomor() + ")");
        System.out.println("Jenis       : " + p.getKendaraan().getJenis());
        System.out.println("Lama Sewa   : " + p.getLamaHari() + " hari");
        System.out.println("Total       : " + Format.rupiah(p.getTotalBiaya()));
        System.out.println("----------------------------------------");
    }
}