package com.kh.menu.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.menu.model.dto.MenuDto;
import com.kh.menu.model.dto.MenuDto.MenuPost;
import com.kh.menu.model.service.MenuService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController	//@ResponseBody + @controller
// 컨트롤러의 모든 메서드에 @ResponsBody를 붙여준다.
public class MenuController {
	/*
	 * 1. REST(Representational State Transfer)
	 *  - 자원의 현재 상태(state)를 어떤 형식으로 전달 하는 규칙, 방법
	 *  
	 *  자원(Resource)
	 *   - 서버에 존재하는 모든 데이터 (문서, 이미지, 사용자 정보)를 의미한다.
	 *   - REST는 모든 자원에 대한 유니크한 URI를 설계하는 것이 원칙이다.
	 *   	ex) 1번 게시글 자원 -> /board/1
	 *   - http 메서드의 전송방식에 따라 같은 URI여도 서로 다른 자원을 가리킨다.
	 *   	GET - 자원 조회
	 *   	POST - 자원 생성
	 *    	PUT/PATCH - 자원 수정
	 *    	DELETE - 자원 삭제
	 *  형식(Representation)
	 *   - 자원의 상태를 표현하는 형식
	 *   - 클라이언트의 요청에 따라 /board/1과 같은 자원은 "xml,json,html" 등 다양한 형식으로  표현될 수 있다.
	 *   - 자원의 형식은 content-type 혹은 request header에 선택된다.
	 *   - REST 방식을 사용하면 URI와 HTTP메서드만 확인시, 어떤 기능을 하는지 쉽게 알 수 있다.
	 *   
	 * #2. REST API
	 *  - REST 아키텍쳐 스타일에 따라 요청한 자원에 대한 CRUD를 진행하는 서버
	 *  - REST 설계 원칙을 잘 준수할수록 RESTful한 API서버라고 부름
	 *  - 일반 MVC컨트롤러는 HTML을 반환하나, REST API는 JSON을 반환한다.
	 * */
	private final MenuService menuService;
	
	/*
	 *  #3. REST API설계 원칙
	 *  1) 명사를 사용하여 자원을 작성한다
	 *   - /getMenus => /menus (복수형으로 작성을 권장)
	 *  2) 새로운 api 생성을 지양한다.
	 *   - 필터용 데이터는 URI가 아닌 쿼리스트링으로 전달한다.
	 *  3) 응답상태를 반드시 전달한다.
	 */
	
	@GetMapping("/menus")
	public ResponseEntity<List<MenuDto.MenuResponse>> menus(
				@RequestParam HashMap<String, Object> param	// 검색 파라미터값
				){
		List<MenuDto.MenuResponse> list = menuService.selectMenus(param);
		
		log.debug("list:{}",list);
		
		return ResponseEntity.ok(list);
	}
	
	// 메뉴등록
	// 4) 행위를 URI에 포함 시키지 않는다.
	//  - /menus/insert -> Post + /menus
	@PostMapping("/menus")
	public ResponseEntity<Void> insertMenu(@RequestBody MenuPost menu){
		int result = menuService.insertMenu(menu);
		
		if(result>0) {
			// Post요청의 경우 응답데이터 header에 이동할 uri정봅를 적어주는 것이 규칙
			URI location = URI.create("/menus/"+menu.getId());
			// 201 Created post 성공
			return ResponseEntity.created(location).build();
		}else {
			// 400 bad Request
			return ResponseEntity.badRequest().build(); //400 bad Request
			
		}
		
	}

}
