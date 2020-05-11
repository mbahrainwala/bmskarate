package ca.bmskarate.service;

import ca.bmskarate.exception.BmsException;
import ca.bmskarate.repositories.CityRepository;
import ca.bmskarate.vo.CityVo;
import ca.bmskarate.vo.ProvinceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    @Transactional
    public List<CityVo> findAll(){return this.cityRepository.findAll();}

    @Transactional
    public Optional<CityVo> findCityById(long id){return this.cityRepository.findById(id);}

    @Transactional
    public CityVo findCityByNameAndProvince(String name, ProvinceVo provinceVo){
        List<CityVo> cityList = cityRepository.findByCityNameAndProvince(name, provinceVo);
        if(cityList!=null && !cityList.isEmpty())
            return cityList.get(0);
        else
            return null;
    }

    @Transactional
    public void addCity(String name, ProvinceVo provinceVo) throws BmsException {
        Assert.notNull(name, "city name cannot be null");
        Assert.notNull(provinceVo, "city province cannot be null");
        CityVo cityVo = findCityByNameAndProvince(name, provinceVo);
        if(cityVo!=null){
            throw new BmsException("Error saving city: city exists");
        }

        CityVo vo = new CityVo();
        vo.setCityName(name);
        vo.setProvince(provinceVo);
        cityRepository.save(vo);
    }
}
