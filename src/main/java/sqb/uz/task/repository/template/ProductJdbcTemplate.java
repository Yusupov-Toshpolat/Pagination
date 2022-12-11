package sqb.uz.task.repository.template;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sqb.uz.task.dto.ProductDTO;
import sqb.uz.task.dto.search.Filter;
import sqb.uz.task.dto.search.Pagination;
import sqb.uz.task.dto.search.Sorting;

import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class ProductJdbcTemplate {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> search(Pagination pagination){
        StringBuilder queryConditions = new StringBuilder(" WHERE 1=1");
        String tableName = pagination.getTableName();

        queryBuilder(queryConditions, pagination.getFilters(), tableName+'.');
        if (pagination.getSorting() != null)
            setSorting(queryConditions, pagination.getSorting());
        if (pagination.getPage() != null && pagination.getSize() != null)
            setPage(queryConditions, pagination.getPage(), pagination.getSize());
        String queryStr = "SELECT * FROM " + tableName + queryConditions;
        log.info("QUERY {}", queryStr);
        List<Map<String, Object>> res = jdbcTemplate.queryForList(queryStr);
        return res;
    }
    private void queryBuilder(StringBuilder queryConditions, List<Filter> filters, String tableName){
        if (filters != null){
            for (Filter filter : filters){
                if (isPresent(filter)){
                    if (filter.getIsBetween())
                        setBetween(queryConditions, filter, tableName);
                    else
                        setCondition(queryConditions, filter, tableName);
                }
            }
        }
    }

    private void setPage(StringBuilder queryConditions, Integer page, Integer size){
        queryConditions.append(" LIMIT " + size + " OFFSET " + (page.equals(0) ? 0 : (page - 1) * size));
    }

    private void setBetween(StringBuilder queryParam, Filter filter, String tableName){
        if (filter.getTypeValue().equals("date"))
            queryParam.append(" AND "+tableName+filter.getFieldName()+ " BETWEEN '"+filter.getBetween().getLow()+"' AND '"+filter.getBetween().getHigh()+"'");
        else
            queryParam.append(" AND "+tableName+filter.getFieldName()+ " BETWEEN "+filter.getBetween().getLow()+" AND "+filter.getBetween().getHigh());
    }

    private void setCondition(StringBuilder queryParam, Filter filter, String tableName){
        if (filter.getTypeValue().equals("date"))
            queryParam.append(" AND "+tableName+filter.getFieldName()+ " = '"+filter.getValue()+"'");
        else if (filter.getTypeValue().equals("string"))
            queryParam.append(" AND "+tableName+filter.getFieldName()+" LIKE '%"+filter.getValue()+"%'");
        else
            queryParam.append(" AND "+tableName+filter.getFieldName()+ " = "+filter.getValue());
    }

    private void setSorting(StringBuilder queryConditions, Sorting sort){
        queryConditions.append(" ORDER BY " + sort.getFieldName() + (sort.getSortType().equals("asc") ? " ASC" : " DESC"));
    }

    private boolean isPresent(Filter filter){
        return filter.getFieldName() != null && filter.getTypeValue() != null && (filter.getValue() != null || filter.getIsBetween());
    }

}
