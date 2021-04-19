import cherrypy
import re, json
from hp_library import hp_database

class HPController(object):

	def __init__(self, hpdb=None):
		if hpdb is None:
			self.hpdb = hp_database()
		else:
			self.hpdb = hpdb

		self.hpdb.load_hp_data('characters.json')

	def GET_INDEX(self):
		output = {'result' : 'success'}

		output['characters'] = []

		try:
			i = 0
			for char in self.hpdb.get_chars():
				i += 1
				char['id'] = i
				output['characters'].append(char)

		except Exception as ex:
			output['result'] = 'error'
			output['message'] = str(ex)

		return json.dumps(output)

	def GET_KEY(self, key):
		output = {'result' : 'success'}

		try:
			key = int(key)
			i = 0
			for char in self.hpdb.get_chars():
				i += 1
				if i == key:
					char['id'] = key
					output['character'] = char
					return json.dumps(output)

			output['result'] = 'error'
			output['message'] = 'character not found'

		except ValueError as ex:
			for char in self.hpdb.get_chars():
				key = key.replace(' ', '').lower()
				if char['name'].replace(' ', '').lower() == key:
					output['character'] = char
					return json.dumps(output)

			output['result'] = 'error'
			output['message'] = 'No character was found.'

		except Exception as ex:
			output['result'] = 'error'
			output['message'] = str(ex)

		return json.dumps(output)

	def PUT_CID(self, cid):
		output = {'result' : 'success'}
		cid = int(cid)

		char = json.loads(cherrypy.request.body.read().decode('utf-8'))

		self.hpdb.set_char(cid - 1, char)

		return json.dumps(output)

	def POST_INDEX(self):
		output = { 'result' : 'success'}
		char = json.loads(cherrypy.request.body.read().decode('utf-8'))

		try:
			self.hpdb.add_char(char)

		except Exception as ex:
			output['result'] = 'error'
			output['message'] = str(ex)

		return json.dumps(output)


	def DELETE_INDEX(self):
		output = {'result' : 'success'}

		try:
			self.hpdb.clear_list()
		except Exception as ex:
			output['result'] ='error'
			output['message'] = str(ex)

		return json.dumps(output)

	def DELETE_KEY(self, key):
		output = {'result' : 'success'}

		try:
			key = int(key)
			self.hpdb.delete_char(key - 1)
		except ValueError as ex:
			i = 0
			for char in self.hpdb.get_chars():
				i += 1
				if char['name'].replace(' ', '').lower() == key:
					self.hpdb.delete_char(i)
					return json.dumps(output)
		except:
			output['result'] ='error'
			output['message'] = str(ex)

		return json.dumps(output)
		
	def PUT_RESET(self):
		output = {'result' : 'success'}
		
		self.hpdb.load_hp_data('characters.json')
		
		return json.dumps(output)

