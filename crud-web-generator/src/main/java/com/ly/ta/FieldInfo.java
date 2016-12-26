package com.ly.ta;

import com.ly.ta.annotation.InputType;

public class FieldInfo {

    private int       order;
    private String    label;
    private String    idTag;
    private String    description;
    private boolean   required;
    private String    name;
    private String    type;
    private InputType inputType;

    //
    private String    uncapitType;
    private boolean   collection;
    private boolean   link;
    private String    capitName;
    private boolean   notNull = false;
    private int       sizeMax = 255;
    private int       sizeMin = 0;
    private boolean   unique  = false;
    private boolean   number  = false;
    private Double    max     = null;
    private Double    min     = null;
    private Double    step    = null;

    public FieldInfo(String name, String type) {
        super();
        this.name = name;
        this.capitName = name.substring(0, 1).toUpperCase() + name.substring(1);
        this.type = type;
        this.collection = false;
        this.link = false;
        this.uncapitType = type.substring(0, 1).toLowerCase() + type.substring(1);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public boolean isLink() {
        return link;
    }

    public void setLink(boolean link) {
        this.link = link;
    }

    public String getCapitName() {
        return capitName;
    }

    public void setCapitName(String capitName) {
        this.capitName = capitName;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public int getSizeMax() {
        return sizeMax;
    }

    public void setSizeMax(int sizeMax) {
        this.sizeMax = sizeMax;
    }

    public int getSizeMin() {
        return sizeMin;
    }

    public void setSizeMin(int sizeMin) {
        this.sizeMin = sizeMin;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public String getUncapitType() {
        return uncapitType;
    }

    public void setUncapitType(String uncapitType) {
        this.uncapitType = uncapitType;
    }

    public boolean isNumber() {
        return number;
    }

    public void setNumber(boolean number) {
        this.number = number;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public boolean getIsMin() {
        return min != null;
    }

    public boolean getIsMax() {
        return max != null;
    }

    public Double getStep() {
        return step;
    }

    public void setStep(Double step) {
        this.step = step;
    }

    public boolean getIsStep() {
        return step != null;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIdTag() {
        return idTag;
    }

    public void setIdTag(String idTag) {
        this.idTag = idTag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public InputType getInputType() {
        return inputType;
    }

    public void setInputType(InputType inputType) {
        this.inputType = inputType;
    }
}
