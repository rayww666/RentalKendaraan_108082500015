package model;

public class Minibus extends Kendaraan implements WajibJaminan {
        private static final long BIAYA_SOPIR_PER_HARI = 150000;
        private static final long UANG_JAMINAN = 1000000;

        public Minibus(String platNomor, String merk, long tarifPerHari) {
                super(platNomor, merk, tarifPerHari);
        }

        @Override
        public String getJenis() {
                return "MINIBUS";
        }

        @Override
        public long hitungBiaya(int hari) {
                return (getTarifPerHari() + BIAYA_SOPIR_PER_HARI) * hari;
        }

        @Override
        public long getUangJaminan() {
                return UANG_JAMINAN;
        }
}