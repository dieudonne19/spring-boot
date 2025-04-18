package org.dd_lgp.com.tutospring.service;

import lombok.RequiredArgsConstructor;
import org.dd_lgp.com.tutospring.dao.operations.StockOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StockService {
    private final StockOperations operation;

    /*
    public StockService(StockOperations operation) {
        this.operation = operation;
    }

     */


}
