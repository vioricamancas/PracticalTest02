package ro.pub.cs.systems.eim.practicaltest02;

public class BitcoinInfo {
    double ToUSD;
    double ToEUR;

    public BitcoinInfo(double toUDS, double toEUR) {
        ToUSD = toUDS;
        ToEUR = toEUR;
    }

    public BitcoinInfo() {
    }

    public double getToUSD() {
        return ToUSD;
    }

    public void setToUSD(double toUSD) {
        ToUSD = toUSD;
    }

    public double getToEUR() {
        return ToEUR;
    }

    public void setToEUR(double toEUR) {
        ToEUR = toEUR;
    }
}
