package ca.bmskarate.service;

import ca.bmskarate.exception.BmsException;
import ca.bmskarate.repositories.ProvinceRepository;
import ca.bmskarate.vo.ProvinceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProvinceService {
    @Autowired
    private ProvinceRepository provinceRepository;

    @Transactional
    public List<ProvinceVo> findAll(){
        return provinceRepository.findAll();
    }

    @Transactional
    public Optional<ProvinceVo> findProvinceById(long id){return this.provinceRepository.findById(id);}

    @Transactional
    public ProvinceVo findProvinceByName(String name){
        List<ProvinceVo> provinceList = provinceRepository.findByProvinceName(name);
        if(provinceList!=null && !provinceList.isEmpty())
            return provinceList.get(0);
        else
            return null;
    }

    @Transactional
    public void addProvince(String name) throws BmsException {
        Assert.notNull(name, "province name cannot be null");
        ProvinceVo provinceVo = findProvinceByName(name);
        if(provinceVo!=null){
            throw new BmsException("Error saving province: province exists");
        }

        ProvinceVo vo = new ProvinceVo();
        vo.setProvinceName(name);
        provinceRepository.save(vo);
    }
}
