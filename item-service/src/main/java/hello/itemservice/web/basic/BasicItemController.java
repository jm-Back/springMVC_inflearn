package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    //스프링 빈에서 주입. itemReposi 에 들어간다.
    //생성자가 하나면, @Autowired 생략 가능.
    //RequiredArgsConstructor가 final 이 붙은 생성자 대신.

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "basic/items";

    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }


//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model){

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item,
                                Model model){
        //모델 객체가 Item 객체 만들어서 저장해줌
        itemRepository.save(item);

//        model.addAttribute("item", item);
//        @ModelAttribute는 모델을 만들고 모델에 넣는 역할도 수행한다.
//        그래서 위에 코드는 자동 추가라 생략 가능하다.


        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item,
                            Model model){
        //이름을 지정하지 않아도 클래스 명의 첫글자를 소문자로 바꿔서 모델 객체에 넣는다.
        //모델 객체가 Item 객체 만들어서 저장해줌
        itemRepository.save(item);

//        model.addAttribute("item", item);
//        @ModelAttribute는 모델을 만들고 모델에 넣는 역할도 수행한다.
//        그래서 위에 코드는 자동 추가라 생략 가능하다.

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV4(Item item, Model model){
        itemRepository.save(item);

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV5(Item item){
        itemRepository.save(item);

        return "redirect:/basic/items/" + item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes){
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId, item); //item 내용이 저장됨.

        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터 추가.
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemaa", 10000, 10));
        itemRepository.save(new Item("item_bbb", 20000, 10));

    }

}
