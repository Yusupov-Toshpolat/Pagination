package sqb.uz.task.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import sqb.uz.task.dto.ProductDTO;
import org.json.JSONObject;
import sqb.uz.task.dto.search.Filter;
import sqb.uz.task.dto.search.Pagination;
import sqb.uz.task.dto.search.Sorting;
import sqb.uz.task.helper.Convert;
import sqb.uz.task.model.Product;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl {

    @Autowired
    private EntityManager entityManager;

    public List<Product> search(){
        Query query = entityManager.createNativeQuery("SELECT * FROM product p WHERE 1=1 AND p.name LIKE '%O%' AND p.price BETWEEN 11999 AND 57999 ORDER BY p.amount ASC", Product.class);
        return query.getResultList();
    }

    public Page<Product> searchByParams(Pagination pagination){
        StringBuilder queryParams = new StringBuilder(" WHERE 1=1");
        String tableShortName = pagination.getTableName().charAt(0) + ".";

        queryBuilder(queryParams, pagination, tableShortName);

        String queryStr = "SELECT * FROM " + pagination.getTableName() + " " + tableShortName.charAt(0) + queryParams;
        String countStr = "SELECT COUNT(1) FROM " + pagination.getTableName() + " " + tableShortName.charAt(0) + queryParams;

        Query query = entityManager.createNativeQuery(queryStr, Product.class);
        Query countQuery = entityManager.createNativeQuery(countStr, Integer.class);

//        setValues(query, pagination.getFilters());
//        setValues(countQuery, pagination.getFilters());
        PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getSize());

        query.setFirstResult(pageRequest.getPageSize() * pageRequest.getPageNumber());
        query.setMaxResults(pageRequest.getPageSize());

        Integer count = countQuery.getFirstResult();
        System.out.println(count + "Data count");
        List<Product> result = query.getResultList();
        List<JSONObject> res = result.stream()
                .map(this::toJsonO).toList();

        return new PageImpl<>(result, pageRequest, count);
    }

    private void queryBuilder(StringBuilder queryParams, Pagination pagination, String tableShortName){
        List<Filter> filters = pagination.getFilters();
        if (filters != null && filters.size() > 0){
            setFilters(queryParams, filters, tableShortName);
        }
        Sorting sorting = pagination.getSorting();
        if (sorting != null)
            setSorting(queryParams, sorting, tableShortName);
    }

    private void setFilters(StringBuilder queryParams, List<Filter> filters, String tableShortName){
        for (Filter filter : filters){
            if (isPresent(filter)){
                if (filter.getIsBetween())
                    setBetween(queryParams, filter, tableShortName);
                else
                    setCondition(queryParams, filter, tableShortName);
            }
        }
    }

    private void setBetween(StringBuilder queryParam, Filter filter, String tableShortName){
        if (filter.getTypeValue().equals("date"))
            queryParam.append(" AND "+tableShortName+filter.getFieldName()+ " BETWEEN '"+filter.getBetween().getLow()+"' AND '"+filter.getBetween().getHigh()+"'");
        else
            queryParam.append(" AND "+tableShortName+filter.getFieldName()+ " BETWEEN "+filter.getBetween().getLow()+" AND "+filter.getBetween().getHigh());
    }
    private void setCondition(StringBuilder queryParam, Filter filter, String tableShortName){
        if (filter.getTypeValue().equals("date"))
            queryParam.append(" AND "+tableShortName+filter.getFieldName()+ " = '"+filter.getValue()+"'");
        else if (filter.getTypeValue().equals("string"))
            queryParam.append(" AND "+tableShortName+filter.getFieldName()+" LIKE '%"+filter.getValue()+"%'");
        else
            queryParam.append(" AND "+tableShortName+filter.getFieldName()+ " = "+filter.getValue());
    }

    private void setSorting(StringBuilder queryParam, Sorting sorting, String tableShortName){
        queryParam.append(" ORDER BY "+tableShortName+sorting.getFieldName()+ (sorting.getSortType().equals("asc") ? " ASC" : "DESC"));
    }

    private void setValues(Query query, List<Filter> filters){
        for (Filter filter : filters){
            if (isPresent(filter)){
                if (filter.getIsBetween())
                    setBetweenValues(query, filter);
                else{
                    switch (filter.getTypeValue()) {
                        case "string" -> query.setParameter(filter.getFieldName(), filter.getValue());
                        case "double" -> query.setParameter(filter.getFieldName(), Convert.toDou(filter.getValue()));
                        case "int" -> query.setParameter(filter.getFieldName(), Convert.toInt(filter.getValue()));
                        case "boolean" -> query.setParameter(filter.getFieldName(), Convert.toBoo(filter.getValue()));
                        case "date" -> query.setParameter(filter.getFieldName(), Convert.toDat(filter.getValue()));
                    }
                }
            }
        }
    }

    private void setBetweenValues(Query query, Filter filter){
        query.setParameter("low_p", filter.getBetween().getLow());
        query.setParameter("high_p", filter.getBetween().getHigh());
    }

    private boolean isPresent(Filter filter){
        return filter.getFieldName() != null && filter.getTypeValue() != null && (filter.getValue() != null || filter.getIsBetween());
    }

    private JSONObject toJsonO(Product product){
        return new JSONObject(product);
    }

}
