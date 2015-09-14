package tilt;

import java.io.Writer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TiltController {

    @RequestMapping("/")
    public String indexPage() {
        return "index";
    }
    
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String showPage() {
        return "dynamic";
    }
  

    @RequestMapping(value = "/safe_dump", method = RequestMethod.GET)
    public String dumpData(@RequestBody String jsonData, @RequestParam("min_score") String minScore ) {
        return jsonData;
    }

    
    /*/////////////////////////////
    
   

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


    @app.route('/scale', methods=['POST'])
    def scale_app():
        new_instances = int(request.form['instances'])
        if ((new_instances > 8) or (new_instances < 1)):
            return "fail"
        else:
            if cf_user:
                client = CloudFoundryClient(cf_user,cf_pass)
                client.authenticate()
                app_data = client.get_app(app_name)
                client.scale_app(app_data['url'], new_instances)

        return "success"


    @app.route('/view')
    def view_redirect():
        return redirect('http://tilt-view.cfapps.io')

    if __name__ == '__main__':
        app.debug = True
        print "Running on Port: " + port
        app.run(host='0.0.0.0', port=int(port))

    
    /////////////////////////////*/

}