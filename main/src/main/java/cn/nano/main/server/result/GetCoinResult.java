package cn.nano.main.server.result;

public class GetCoinResult extends BaseResult {

    /**
     * data : {"id":"455","coin":50,"time":300}
     */

    private CoinConfigBean data;

    public CoinConfigBean getData() {
        return data;
    }

    public void setData(CoinConfigBean data) {
        this.data = data;
    }
}
