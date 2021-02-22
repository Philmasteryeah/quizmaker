package org.philmaster.quizmaker.controller.web;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.ArrayList;
import java.util.List;

import org.philmaster.quizmaker.model.User;
import org.philmaster.quizmaker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping
public class WebUserController {

	@Autowired
	UserService userService;

	@GetMapping(value = "/user/{user_id}/quizzes")
	@PreAuthorize("permitAll")
	public String getQuizzesForUser(@PathVariable Long user_id) {
		userService.find(user_id);

		// TODO: Unimplemented
		return "error/404";
	}

	@GetMapping(value = "/userList")
	@PreAuthorize("permitAll")
	public String getUserList(Model model,
			@PageableDefault(page = 0, size = 10) @SortDefault.SortDefaults({
					@SortDefault(sort = "username", direction = Direction.DESC),
					@SortDefault(sort = "email", direction = Direction.ASC) }) Pageable pageable) {
		Page<User> userList = userService.findAll(pageable);

		//PageWrapper<User> page = new PageWrapper<User>(userList, "/userList");
		
		model.addAttribute("userList", userList);
		
		
		return "pages/userList";
	}

	public class PageWrapper<T> {
		public static final int MAX_PAGE_ITEM_DISPLAY = 5;
		private Page<T> page;
		private List<PageItem> items;
		private int currentNumber;
		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public PageWrapper(Page<T> page, String url) {
			this.page = page;
			this.url = url;
			items = new ArrayList<PageItem>();

			currentNumber = page.getNumber() + 1; // start from 1 to match page.page

			int start, size;
			if (page.getTotalPages() <= MAX_PAGE_ITEM_DISPLAY) {
				start = 1;
				size = page.getTotalPages();
			} else {
				if (currentNumber <= MAX_PAGE_ITEM_DISPLAY - MAX_PAGE_ITEM_DISPLAY / 2) {
					start = 1;
					size = MAX_PAGE_ITEM_DISPLAY;
				} else if (currentNumber >= page.getTotalPages() - MAX_PAGE_ITEM_DISPLAY / 2) {
					start = page.getTotalPages() - MAX_PAGE_ITEM_DISPLAY + 1;
					size = MAX_PAGE_ITEM_DISPLAY;
				} else {
					start = currentNumber - MAX_PAGE_ITEM_DISPLAY / 2;
					size = MAX_PAGE_ITEM_DISPLAY;
				}
			}

			for (int i = 0; i < size; i++) {
				items.add(new PageItem(start + i, (start + i) == currentNumber));
			}
		}

		public List<PageItem> getItems() {
			return items;
		}

		public int getNumber() {
			return currentNumber;
		}

		public List<T> getContent() {
			return page.getContent();
		}

		public int getSize() {
			return page.getSize();
		}

		public int getTotalPages() {
			return page.getTotalPages();
		}

		public boolean isFirstPage() {
			return page.isFirst();
		}

		public boolean isLastPage() {
			return page.isLast();
		}

		public boolean isHasPreviousPage() {
			return page.hasPrevious();
		}

		public boolean isHasNextPage() {
			return page.hasNext();
		}

		public class PageItem {
			private int number;
			private boolean current;

			public PageItem(int number, boolean current) {
				this.number = number;
				this.current = current;
			}

			public int getNumber() {
				return this.number;
			}

			public boolean isCurrent() {
				return this.current;
			}
		}
	}

}
