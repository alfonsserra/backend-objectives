package com.systelab.seed.objective.model;

import com.systelab.seed.infrastructure.ModelBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Audited
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "objective")
public class Objective extends ModelBase {

    @NotNull
    @Size(min = 1, max = 255)
    @ApiModelProperty(value = "Objective name", example = "Be more efficient", required = true)
    public String name;

    @Min(0)
    @Max(10)
    @ApiModelProperty(value = "Objective evaluation", example = "1", required = true)
    public Integer evaluation;
}