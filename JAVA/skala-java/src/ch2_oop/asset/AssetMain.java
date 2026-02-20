package ch2_oop.asset;

public class AssetMain {

    public static void main(String[] args) {
        Stock samsung = new Stock("삼성전자", 70000);
        PreferredStock lgPreferred = new PreferredStock("LG전자(우)", 83000, 4.5);

        samsung.printInfo();
        lgPreferred.printInfo();

        // 가격 변경 후 정보 출력
    }
}
