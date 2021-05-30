package com.apptech.lava_retailer.list;

public class ClaimTypeList {
   String claim_type ;

    public ClaimTypeList(String claim_type) {
        this.claim_type = claim_type;
    }

    public String getClaim_type() {
        return claim_type;
    }

    public void setClaim_type(String claim_type) {
        this.claim_type = claim_type;
    }

    @Override
    public String toString() {
        return claim_type;
    }
}
