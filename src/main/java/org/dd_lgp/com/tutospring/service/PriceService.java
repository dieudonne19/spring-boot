package org.dd_lgp.com.tutospring.service;

import lombok.RequiredArgsConstructor;
import org.dd_lgp.com.tutospring.dao.operations.PriceOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PriceService  {
    private final PriceOperations operation;

    /*
    public PriceService(PriceOperations operation) {
        this.operation = operation;
    }
     */
}
