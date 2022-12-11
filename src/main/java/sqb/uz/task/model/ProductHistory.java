package sqb.uz.task.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_history")
public class ProductHistory {
    @Id
    @GeneratedValue(generator = "p_history_id_seq")
    @SequenceGenerator(name = "p_history_id_seq", sequenceName = "p_history_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "p_id")
    private Long productId;

    @Column(name = "action")
    private String action;

    @Column(name = "action_date")
    private LocalDateTime actionDate;
}
