package com.devsuperior.bds04.services;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.repositories.CityRepository;
import com.devsuperior.bds04.repositories.EventRepository;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;

@Service
public class EventService {

	@Autowired
	private EventRepository repository;
	
	@Autowired
	private CityRepository cityRepository;

	@Transactional
	public EventDTO insert(@Valid EventDTO dto) {
		var event = new Event();
		
		event.setDate(dto.getDate());
		event.setName(dto.getName());
		event.setUrl(dto.getUrl());
		
		var city = cityRepository.findById(dto.getCityId()).orElseThrow(() -> new ResourceNotFoundException("Entity not found for id " + dto.getCityId()));
		
		event.setCity(city);
		event = repository.save(event);
		return new EventDTO(event);
		
	}

	@Transactional(readOnly = true)
	public Page<EventDTO> findAll(Pageable pageable) {
		var events = repository.findAll(pageable);
		return events.map(event -> new EventDTO(event));
	}
}
