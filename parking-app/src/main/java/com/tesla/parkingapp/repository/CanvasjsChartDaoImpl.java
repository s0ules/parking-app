package com.tesla.parkingapp.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tesla.parkingapp.model.CanvasjsChartData;

@Repository("canvasjsChartDao")
public class CanvasjsChartDaoImpl implements CanvasjsChartDao {
 
	@Override
	public List<List<Map<Object, Object>>> getCanvasjsChartData() {
		return CanvasjsChartData.getCanvasjsDataList();
	}
 
}                        