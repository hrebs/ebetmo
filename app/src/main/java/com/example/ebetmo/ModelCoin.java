package com.example.ebetmo;

public class ModelCoin {
    private int coin_id;
    private String owner_id;
    private String amount;
    private String date;

    public ModelCoin(int coin_id, String owner_id, String amount, String date) {
        this.coin_id = coin_id;
        this.owner_id = owner_id;
        this.amount = amount;
        this.date = date;
    }

    public int getCoin_id() {
        return coin_id;
    }

    public void setCoin_id(int coin_id) {
        this.coin_id = coin_id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
