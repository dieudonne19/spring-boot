package org.dd_lgp.com.tutospring.dao.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dd_lgp.com.tutospring.model.Price;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PriceMapper implements Function<ResultSet, Price> {

    @Override
    @SneakyThrows
    public Price apply(ResultSet resultSet) {
        Price price = new Price();
        price.setId(resultSet.getLong("id"));
        price.setAmount(resultSet.getDouble("amount"));
        price.setDateValue(resultSet.getDate("date_value").toLocalDate());
        return price;
    }

}
