

package com.revolut.transferservice.api.currencies;

import com.revolut.transferservice.api.Validatable;

public interface Currency extends Validatable {

    int ISO_CODE_LENGTH = 3;

    String getISOCode();
    
    String getCountry();
}
