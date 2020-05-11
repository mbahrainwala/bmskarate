package ca.bmskarate.controller.response;

import ca.bmskarate.vo.CityVo;
import ca.bmskarate.vo.ProvinceVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HeartBeatResponse {
    List<ProvinceVo> provinceList;
    List<CityVo> cityList;
    List<HeartBeatResponseSecQues> secQues;
    List<HeartBeatResponseUserTypes> userTypes;
    List<HeartBeatResponseBeltTypes> belts;
}
