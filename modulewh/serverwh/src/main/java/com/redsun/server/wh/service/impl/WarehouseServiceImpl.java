

package com.redsun.server.wh.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.redsun.server.wh.model.Warehouse;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.WarehouseRepository;
import com.redsun.server.wh.repository.specification.WarehouseSpecification;
import com.redsun.server.wh.repository.specification.WarehouseSpecificationsBuilder;
import com.redsun.server.wh.service.WarehouseService;

@Service("warehouse")
@Transactional
public class WarehouseServiceImpl implements WarehouseService {
	
	@Autowired
	private WarehouseRepository warehouseRepository;
	
	@Autowired
	private WarehouseSpecificationsBuilder warehouseSpecificationsBuilder;

	@Override
	public Warehouse save(Warehouse warehouse) {
		return warehouseRepository.save(warehouse);
	}

	@Override
	public Warehouse create(Warehouse warehouse) {
		return warehouseRepository.save(warehouse);
	}

	@Override
	public Warehouse update(Integer id, Warehouse warehouse) {
		return warehouseRepository.save(warehouse);
	}

	@Override
	public void delete(Warehouse warehouse) {
		warehouseRepository.delete(warehouse);
		
	}

	@Override
	public void deleteById(Integer id) {
		warehouseRepository.delete(id);
		
	}

	@Override
	public Warehouse getById(Integer id) {
		return warehouseRepository.findOne(id);
	}

	@Override
	public List<Warehouse> listAll() {
		return warehouseRepository.findAll();
	}

	@Override
	public long countAll() {
		return warehouseRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return warehouseRepository.exists(id);
	}

	@Override
	public List<Warehouse> listWithCritera(SearchCriteria searchCriteria) {
		
		Specification<Warehouse> warehouseSpecification = new WarehouseSpecification(searchCriteria);
        List<Warehouse> result = warehouseRepository.findAll(warehouseSpecification);
        return result;
        
	}

	@Override
	public List<Warehouse> listWithCriteras(List<SearchCriteria> searchCriterias) {
		
		Specification<Warehouse> warehouseSpecification = warehouseSpecificationsBuilder.build(searchCriterias);
        List<Warehouse> result = warehouseRepository.findAll(warehouseSpecification);
        return result;
        
	}

	@Override
	public Page<Warehouse> listAllByPage(Pageable pageable) {
		
		return warehouseRepository.findAll(pageable);
		
	}

	@Override
	public Page<Warehouse> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		
		Specification<Warehouse> warehouseSpecification = new WarehouseSpecification(searchCriteria);
		Page<Warehouse> result = warehouseRepository.findAll(warehouseSpecification, pageable);
        return result;
        
	}

	@Override
	public Page<Warehouse> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		
		Specification<Warehouse> warehouseSpecification = warehouseSpecificationsBuilder.build(searchCriterias);
		Page<Warehouse> result = warehouseRepository.findAll(warehouseSpecification, pageable);
        return result;
        
	}
}
