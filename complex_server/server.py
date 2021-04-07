import routes
import cherrypy
#Change this to the class name of our controller 
from hpController import HPController 

def start_service():
    #Change this to the class name of our controller 
    hpController = HPController()

    #create dispatcher
    dispatcher = cherrypy.dispatch.RoutesDispatcher()

    #use dispatcher to connect resources to event handlers
    #connect(out_tag, http resource, class object with handler, event handler name, what type of HTTP request to serve)
    #dispatcher.connect('hp_get_cid','/hp/:cid', controller=hpController, action='GET_CID', conditions=dict(method=['GET']))
    #get_index
    dispatcher.connect('hp_get_all','/hp/', controller=hpController, action='GET_INDEX', conditions=dict(method=['GET']))
    #get_name
    #dispatcher.connect('hp_get_character_name','/hp/:char_name', controller=hpController, action='GET_NAME', conditions=dict(method=['GET']))
    dispatcher.connect('hp_get_character_name','/hp/:key', controller=hpController, action='GET_KEY', conditions=dict(method=['GET']))
    #delete_cid
    #dispatcher.connect('hp_delete_key','/hp/:cid', controller=hpController, action='DELETE_CID', conditions=dict(method=['DELETE']))
    dispatcher.connect('hp_delete_key','/hp/:key', controller=hpController, action='DELETE_KEY', conditions=dict(method=['DELETE']))
    #delete_all
    dispatcher.connect('hp_delete_all','/hp/', controller=hpController, action='DELETE_INDEX', conditions=dict(method=['DELETE']))
    #delete_name
    #dispatcher.connect('hp_delete_character_name','/hp/:char_name', controller=hpController, action='DELETE_NAME', conditions=dict(method=['DELETE']))
    #post_new_character
    dispatcher.connect('hp_post_new','/hp/', controller=hpController, action='POST_INDEX', conditions=dict(method=['POST']))
    #put_index
    dispatcher.connect('hp_put_cid','/hp/:cid', controller=hpController, action='PUT_CID', conditions=dict(method=['PUT']))
    #put_index
    dispatcher.connect('hp_reset_data','/hp/reset/', controller=hpController, action='PUT_RESET', conditions=dict(method=['PUT']))

    # default OPTIONS handler for CORS, all direct to the same place
    dispatcher.connect('hp_options', '/hp/', controller=optionsController, action='OPTIONS', conditions=dict(method=['OPTIONS']))
    dispatcher.connect('hp_cid_options', '/hp/:key', controller=optionsController, action='OPTIONS', conditions=dict(method=['OPTIONS']))
    dispatcher.connect('hp_name_options', '/hp/:cid', controller=optionsController, action='OPTIONS', conditions=dict(method=['OPTIONS']))
    dispatcher.connect('hp_reset_options', '/hp/reset/', controller=optionsController, action='OPTIONS', conditions=dict(method=['OPTIONS']))
    #set up configuration
    conf = {
        'global' : {
            'server.socket_host' : 'localhost', #'student04.cse.nd.edu',
            #Change this to the test port when testing
            'server.socket_port' : 51040,
            },
        '/' : {
            'request.dispatch' : dispatcher,
            'tools.CORS.on' : True, # configuration for CORS
            }
    }

    #update with new configuration
    cherrypy.config.update(conf)
    app = cherrypy.tree.mount(None, config=conf) # create app
    cherrypy.quickstart(app)    # start app

# class for CORS
class optionsController:
    def OPTIONS(self, *args, **kwargs):
        return ""

# function for CORS
def CORS():
    cherrypy.response.headers["Access-Control-Allow-Origin"] = "*"
    cherrypy.response.headers["Access-Control-Allow-Methods"] = "GET, PUT, POST, DELETE, OPTIONS"
    cherrypy.response.headers["Access-Control-Allow-Credentials"] = "true"


if __name__ == '__main__':
    cherrypy.tools.CORS = cherrypy.Tool('before_finalize', CORS) # CORS
    start_service()