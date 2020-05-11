package ca.bmskarate.controller;

import ca.bmskarate.controller.response.HeartBeatResponse;
import ca.bmskarate.controller.response.HeartBeatResponseBeltTypes;
import ca.bmskarate.controller.response.HeartBeatResponseSecQues;
import ca.bmskarate.controller.response.HeartBeatResponseUserTypes;
import ca.bmskarate.exception.BmsException;
import ca.bmskarate.service.CityService;
import ca.bmskarate.service.ProvinceService;
import ca.bmskarate.service.UserService;
import ca.bmskarate.util.Belt;
import ca.bmskarate.util.YesNo;
import ca.bmskarate.vo.CityVo;
import ca.bmskarate.vo.ProvinceVo;
import ca.bmskarate.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HeartBeatController {
    @Autowired
    UserService userService;

    @Autowired
    CityService cityService;

    @Autowired
    ProvinceService provinceService;

    private static final String DEFAULT_PROVINCE_SETUP="Ontario";
    private static final String DEFAULT_CITY_SETUP="Vaughan";
    private static final String DEFAULT_SUPER_USER="louis@bmskarate.com";

    @RequestMapping(value = "/heartBeat", method = RequestMethod.GET)
    public ResponseEntity<HeartBeatResponse> heartBeat() throws BmsException {

        ProvinceVo provinceVo = provinceService.findProvinceByName(DEFAULT_PROVINCE_SETUP);
        if(provinceVo==null) {
            provinceService.addProvince(DEFAULT_PROVINCE_SETUP);
            provinceVo = provinceService.findProvinceByName(DEFAULT_PROVINCE_SETUP);
            //TODO: Optimize the city and user creation here.
        }

        CityVo cityVo = cityService.findCityByNameAndProvince(DEFAULT_CITY_SETUP, provinceVo);
        if(cityVo==null)
            cityService.addCity(DEFAULT_CITY_SETUP, provinceVo);

        UserVo sUser = userService.getUserByEmail(DEFAULT_SUPER_USER);

        List<UserVo> u1=userService.getUserByLastNameLike("K%");
        List<UserVo> u2=userService.getUserByLastNameLike("A%");

        if(sUser==null)
        {
            sUser = new UserVo();
            sUser.setEmailId(DEFAULT_SUPER_USER);
            sUser.setFirstName("Louis");
            sUser.setLastName("Karate");
            sUser.setSesnei(YesNo.Y.toString());
            sUser.setAddr1("71 Innovation Drive");
            sUser.setPostalCode("L4H0S3");
            sUser.setPhone(9058513555L);
            sUser.setType(UserService.AllowedUserTypes.S.toString());
            sUser.setSecretQues(UserService.SecretQuestions.A.toString());
            sUser.setSecAnsAsEncrypt("BMS");

            if(cityVo==null)
                cityVo = cityService.findCityByNameAndProvince(DEFAULT_CITY_SETUP, provinceVo);

            sUser.setCityVo(cityVo);
            userService.saveUser(sUser);

            sUser = userService.getUserByEmail(DEFAULT_SUPER_USER);
            sUser.setPasswordAsEncrypt("SuperBMS!");
            userService.saveUser(sUser);
        }

        HeartBeatResponse resp = new HeartBeatResponse();
        resp.setProvinceList(provinceService.findAll());
        resp.setCityList(cityService.findAll());

        List<HeartBeatResponseSecQues> secQues = new ArrayList<>();
        List<HeartBeatResponseUserTypes> userTypes = new ArrayList<>();
        List<HeartBeatResponseBeltTypes> belts = new ArrayList<>();

        HeartBeatResponseSecQues ques = null;
        for(UserService.SecretQuestions values:UserService.SecretQuestions.values()){
            ques = new HeartBeatResponseSecQues();
            ques.setSecQuesCode(values.toString());
            ques.setSecQuesDesc(values.getDescription());
            secQues.add(ques);
        }
        resp.setSecQues(secQues);

        HeartBeatResponseUserTypes ut = null;
        for(UserService.AllowedUserTypes values:UserService.AllowedUserTypes.values()){
            ut = new HeartBeatResponseUserTypes();
            ut.setUserTypeCode(values.toString());
            ut.setUserTypeDesc(values.getDescription());
            userTypes.add(ut);
        }
        resp.setUserTypes(userTypes);

        HeartBeatResponseBeltTypes belt = null;
        for(Belt values:Belt.values()){
            belt = new HeartBeatResponseBeltTypes();
            belt.setBeltId(values.getId());
            belt.setBeltColor(values.toString());
            belts.add(belt);
        }
        resp.setBelts(belts);

        return ResponseEntity.ok(resp);
    }
}
