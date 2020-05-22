package dosu.com.final_project.Controller;


import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.ArrayUtils;

import java.net.InetAddress;

@RequestMapping("/")
@Controller
public class ScreenController {

    @GetMapping("/")
    public ModelAndView index(){
        return new ModelAndView("index");
    }

    @PostMapping("/")
    public ModelAndView indexPost(int row, int column){
        Integer[] arr = {1,2,3,4};
        if (ArrayUtils.contains(arr,row)&ArrayUtils.contains(arr,column)) {
            int coordinates = row*10 + column;
            return new ModelAndView("redirect:/"+coordinates);
        }
        else{
            return new ModelAndView("redirect:/");
        }
    }

    @GetMapping("/{coordinates}")
    public ModelAndView screen(@PathVariable("coordinates") int coordinates){
        double delay = 0.0;
        try {
            String TIME_SERVER = "time-a.nist.gov";
            NTPUDPClient timeClient = new NTPUDPClient();
            InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
            TimeInfo timeInfo = timeClient.getTime(inetAddress);
            Long returnTime = timeInfo.getReturnTime();
            Long delayLong = returnTime%3000;
            delay = -delayLong.doubleValue()/1000;
            System.out.println("Delay : " + delay);
        }
        catch (Exception e){
            e.fillInStackTrace();
        }

        ModelAndView modelAndView = new ModelAndView("screen");
        modelAndView.addObject("coordinates",coordinates);
        modelAndView.addObject("delay",delay);
        return modelAndView;
    }
}
