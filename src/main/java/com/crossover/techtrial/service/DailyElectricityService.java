package com.crossover.techtrial.service;

import java.util.List;

/**
 * Created by Abubakar.Saifullah on 10-Jun-18.
 */

public interface DailyElectricityService {
    public List<Object[]> getDailyElectricity(Long panelId, String date);
}
