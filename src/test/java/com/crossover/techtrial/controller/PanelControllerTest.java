package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.HourlyElectricity;
import com.crossover.techtrial.model.Panel;
import com.crossover.techtrial.repository.HourlyElectricityRepository;
import com.crossover.techtrial.repository.PanelRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.crossover.techtrial.controller.utility.Utility.APPLICATION_JSON_UTF8;
import static com.crossover.techtrial.controller.utility.Utility.convertObjectToJsonBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * PanelControllerTest class will test all APIs in PanelController.java.
 * @author Crossover
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PanelControllerTest {


  private String serialNumber;
  private static final Double DEFAULT_LATITUDE = 54.123232;
  private static final Double DEFAULT_LONGITUDE = 54.123232;
  private static final String DEFAULT_BRAND = "teslaS";


  private Panel panel;
  
  private MockMvc mockMvc;

  @Autowired private TestRestTemplate template;
  @Autowired private PanelController panelController;
  @Autowired private PanelRepository panelRepository;
    @Autowired private HourlyElectricityRepository hourlyElectricityRepository;


  @Before
  public void setup() throws Exception {
    mockMvc = MockMvcBuilders.standaloneSetup(panelController).build();
  }

  //Panel entity creator can be user throughout the application
  public Panel createPanel(){
    Panel panel = new Panel();
    panel.setLatitude(DEFAULT_LATITUDE);
    panel.setLongitude(DEFAULT_LONGITUDE);
    panel.setBrand(DEFAULT_BRAND);
    serialNumber = generateRandomString(16);
    panel.setSerial(serialNumber);
    return panel;
  }

    //HourlyElectricity entity creator can be user throughout the application
    public HourlyElectricity createHourlyElectricity(){
        HourlyElectricity hourlyElectricity = new HourlyElectricity();
        panel.setLatitude(DEFAULT_LATITUDE);
        panel.setLongitude(DEFAULT_LONGITUDE);
        panel.setBrand(DEFAULT_BRAND);
        serialNumber = generateRandomString(16);
        panel.setSerial(serialNumber);
        return hourlyElectricity;
    }


  @Test
  public void testPanelShouldBeRegistered() throws Exception {

    int totalBeforeInsertion =  Long.valueOf(panelRepository.count()).intValue();

    mockMvc.perform(post("/api/register",1)
            .contentType(APPLICATION_JSON_UTF8).content(convertObjectToJsonBytes(createPanel())))
            .andExpect(status().isCreated());
    List<Panel> panels =  panelRepository.findAll();

    assertThat(panels).hasSize(totalBeforeInsertion + 1);
    Panel panel = panels.get(panels.size() - 1);
    assertThat(panel.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
    assertThat(panel.getLatitude()).isEqualTo(DEFAULT_LONGITUDE);
    assertThat(panel.getBrand()).isEqualTo(DEFAULT_BRAND);
    assertThat(panel.getSerial()).isEqualTo(getSerialNumber());
  }

  @Test
  public void testAllDailyElectricityFromYesterday() throws Exception {
        List<Panel> panels =  panelRepository.findAll();
        if(panels.size() > 0) {
            mockMvc.perform(get("/api/panels/{panel-serial}/daily",panels.get(0).getSerial()))
                    .andExpect(status().isOk());
        }

  }

  @Test
  public void testSaveHourlyElectricity() throws Exception {

      List<Panel> panels =  panelRepository.findAll();

      HourlyElectricity hourlyElectricity = new HourlyElectricity();
      hourlyElectricity.setPanel(panels.get(0));
      hourlyElectricity.setGeneratedElectricity(12L);
      int totalBeforeInsertion =  Long.valueOf(hourlyElectricityRepository.count()).intValue();
      mockMvc.perform(post("/api/panels/{panel-serial}/hourly",panels.get(0).getSerial())
                .contentType(APPLICATION_JSON_UTF8).content(convertObjectToJsonBytes(hourlyElectricity)))
                .andExpect(status().isCreated());
      List<HourlyElectricity> hourlyElectricities =  hourlyElectricityRepository.findAll();

      assertThat(hourlyElectricities).hasSize(totalBeforeInsertion + 1);
      HourlyElectricity electricity = hourlyElectricities.get(hourlyElectricities.size() - 1);
      assertThat(electricity.getGeneratedElectricity()).isEqualTo(12L);
  }

  private HttpEntity<Object> getHttpEntity(Object body) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<Object>(body, headers);
  }


    // Getting random 16 digit serial number
    public static String generateRandomString(int length) {

        Random random = new Random((new Date()).getTime());
        char[] values = {'a','b','c','d','e','f','g','h','i','j',
                'k','l','m','n','o','p','q','r','s','t',
                'u','v','w','x','y','z','0','1','2','3',
                '4','5','6','7','8','9'};
        String out = "";
        for (int i=0;i<length;i++) {
            int idx=random.nextInt(values.length);
            out += values[idx];
        }
        return out;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
