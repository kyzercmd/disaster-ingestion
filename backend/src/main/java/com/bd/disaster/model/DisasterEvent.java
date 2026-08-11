package com.bd.disaster.model;

public class DisasterEvent {
    private String eventId;
    private String hazardType;
    private String startDate;
    private String endDate;
    private String division;
    private String district;
    private String upazila;
    private Long affectedPeople;
    private Long deaths;
    private Double economicLossBdt;
    private String sourceUrl;
    private Double economicLossUsd;
    private Integer durationDays;
    private String severityCategory;
    private String notes;

    public DisasterEvent() {}

    public String getEventId()           { return eventId; }
    public String getHazardType()        { return hazardType; }
    public String getStartDate()         { return startDate; }
    public String getEndDate()           { return endDate; }
    public String getDivision()          { return division; }
    public String getDistrict()          { return district; }
    public String getUpazila()           { return upazila; }
    public Long getAffectedPeople()      { return affectedPeople; }
    public Long getDeaths()              { return deaths; }
    public Double getEconomicLossBdt()   { return economicLossBdt; }
    public String getSourceUrl()         { return sourceUrl; }
    public Double getEconomicLossUsd()   { return economicLossUsd; }
    public Integer getDurationDays()     { return durationDays; }
    public String getSeverityCategory()  { return severityCategory; }
    public String getNotes()             { return notes; }

    public void setEventId(String eventId)                  { this.eventId = eventId; }
    public void setHazardType(String hazardType)            { this.hazardType = hazardType; }
    public void setStartDate(String startDate)              { this.startDate = startDate; }
    public void setEndDate(String endDate)                  { this.endDate = endDate; }
    public void setDivision(String division)                { this.division = division; }
    public void setDistrict(String district)                { this.district = district; }
    public void setUpazila(String upazila)                  { this.upazila = upazila; }
    public void setAffectedPeople(Long affectedPeople)      { this.affectedPeople = affectedPeople; }
    public void setDeaths(Long deaths)                      { this.deaths = deaths; }
    public void setEconomicLossBdt(Double economicLossBdt)  { this.economicLossBdt = economicLossBdt; }
    public void setSourceUrl(String sourceUrl)              { this.sourceUrl = sourceUrl; }
    public void setEconomicLossUsd(Double economicLossUsd)  { this.economicLossUsd = economicLossUsd; }
    public void setDurationDays(Integer durationDays)       { this.durationDays = durationDays; }
    public void setSeverityCategory(String severityCategory){ this.severityCategory = severityCategory; }
    public void setNotes(String notes)                      { this.notes = notes; }
}
