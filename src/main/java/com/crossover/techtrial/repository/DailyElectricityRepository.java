package com.crossover.techtrial.repository;

import com.crossover.techtrial.dto.DailyElectricity;
import com.crossover.techtrial.model.HourlyElectricity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Abubakar.Saifullah on 09-Jun-18.
 */

@Repository
public interface DailyElectricityRepository extends PagingAndSortingRepository<HourlyElectricity,Long> {

    @Query(value="select DATE_FORMAT(reading_at, '%Y-%m-%d') as date, min(generated_electricity)," +
            " max(generated_electricity), sum(generated_electricity), avg(generated_electricity)" +
            " from crosssolar.hourly_electricity where panel_id = :panel_id" +
            " and DATE_FORMAT(reading_at, '%Y-%m-%d') >= :date" +
            " and DATE_FORMAT(reading_at, '%Y-%m-%d') < DATE_FORMAT(current_date, '%Y-%m-%d')" +
            " group by DATE_FORMAT(reading_at, '%Y-%m-%d') order by reading_at asc", nativeQuery = true)
    List<Object[]> getDailyElectricity(@Param("panel_id") long panel_id, @Param("date") String date);
}
