package com.crossover.techtrial.service;

import com.crossover.techtrial.repository.DailyElectricityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by Abubakar.Saifullah on 10-Jun-18.
 */

@Service
public class DailyElectricityServiceImpl implements DailyElectricityService {

    @Autowired
    DailyElectricityRepository dailyElectricityRepository;

    public List<Object[]> getDailyElectricity(Long panelId, String date) {
        return dailyElectricityRepository.getDailyElectricity(panelId, date);
    }
}