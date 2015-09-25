package tilt;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;


import org.springframework.ui.Model;


@Controller
public class TiltController {

    @RequestMapping("/")
    public String indexPage() {
        return "index";
    }
    
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String showPage(String myport, Model model) {
    	myport=System.getenv("PORT");
    	model.addAttribute("port",myport);
    	return "dynamic";
    }
  
    @RequestMapping(value = "/scale", method = RequestMethod.POST)
    public String scaleInstances(@RequestParam(value="instances") String requestedInstances, Model model) {
    	int instances=Integer.parseInt(requestedInstances);
    	if ((instances > 8) || (instances < 1)) {
            return "fail";
    	} else {
    		if (System.getenv("VCAP_APPLICATION") != null) {
    			JsonParser jp=JsonParserFactory.getJsonParser();
    			Map<String,Object> map = jp.parseMap(System.getenv("VCAP_APPLICATION"));
    			String app_name=map.get("application_name").toString();
                String app_url=map.get("application_uris").toString();
    		}
    		if (System.getenv("customconfig") != null) {
    			JsonParser jp=JsonParserFactory.getJsonParser();
    			Map<String,Object> map = jp.parseMap(System.getenv("customerconfig"));
    		    String cf_user = map.get("cfuser").toString();
    		    String cf_pass = map.get("cfpass").toString();
    		    /*if (cf_user != null) {
                    client = CloudFoundryClient(cf_user,cf_pass)
                    client.authenticate()
                    client.scale_app(app_url, instances)*/
              }
    	}
                return "success";
    	}
    
    /*/////////////////////////////
    @RequestMapping(value = "/safe_dump", method = RequestMethod.GET)
    public String dumpData(@RequestBody String jsonData, @RequestParam("min_score") String minScore ) {
        return jsonData;
    }

   

    @app.route('/send', methods=['POST'])
    def receive_post_data():
        if request.method == 'POST':
            current_time = timestamp()
            client_data = json.loads(request.form['data'])

            #  Sanitize numerical data, so any "None" or Null values become 0's
            for key in ["TiltFB","TiltLR","Direction","altitude","latitude","longitude"]:
                if client_data[key] == None:
                    print "Sanitized: %s on %s" % (key, client_data['devid'])
                    client_data[key] = 0

            client_data['timestamp'] = current_time

            # Key is devid:<UUID>, expires in 3 seconds
            r.zadd('devid:' + client_data['devid'],
                   json.dumps(client_data), current_time)
            r.expire('devid:' + client_data['devid'], 3)

            # Update # of connections processed
            r.incr('server:' + port)
            r.expire('server:' + port, 3)
            return "success"
        return "fail"


    @app.route('/show')
    def show():
        return render_template('dynamic.html')


    @app.route('/safe_dump', methods=['GET', 'POST'])
    def safe_dump():
        min_score = int(request.args.get('min_score', 0))
        valid_keys = r.keys('devid:*')
        data = list()
        instances = list()
        max_score = timestamp()
        for key in valid_keys:
            data.extend(r.zrangebyscore(key, min_score, max_score))
        for key in r.keys('server:*'):
            inst = "%s:%s" % (key, r.get(key))
            instances.append(inst)
        return jsonify(timestamp=max_score, data=data, min_score=min_score,
                       instance=instances)


   


    @app.route('/view')
    def view_redirect():
        return redirect('http://tilt-view.cfapps.io')

    if __name__ == '__main__':
        app.debug = True
        print "Running on Port: " + port
        app.run(host='0.0.0.0', port=int(port))

    
    /////////////////////////////*/

}