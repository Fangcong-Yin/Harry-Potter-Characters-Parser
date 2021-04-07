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
		
	def GET_KEY(self, key):
		try:
			cid = int(key)
			GET_CID(self, cid)
		except ValueError as e:
			GET_NAME(self, key)


	def GET_CID(self, char_id):
		output = {'result' : 'success'}
		char_id = int(char_id)

		try:
			char = self.hpdb.get_char(char_id)
			if char is not None:
				output['id'] = char_id
				output['title'] = char

			else:
				output ['result'] = 'error'
				output['message'] = 'movie not found'

		except Exception as ex:
			output['result'] = 'error'
			output['message'] = str(ex)

		return json.dumps(output)

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

	def GET_NAME(self, char_name):
		output = {'result' : 'success'}

		for char in self.hpdb.get_chars():
			if char['name'].replace(' ', '').lower() == char_name:
				output['character'] = char
				return json.dumps(output)

		output['result'] = 'error'
		output['message'] = 'No character named ' + char_name + 'was found.'

		return json.dumps(output)

	def PUT_CID(self, char_id):
		output = {'result' : 'success'}
		char_id = int(char_id)

		char = json.loads(cherrypy.request.body.read().decode('utf-8'))

		self.hpdb.set_char(char_id, char)

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
	
	def DELETE_KEY(self, key):
		try:
			cid = int(key)
			DELETE_CID(self, cid)
		except ValueError as e:
			DELETE_NAME(self, key)

	def DELETE_CID(self, char_id):
		output = {'result' : 'success'}

		char_id = int(char_id)

		try:
			self.hpdb.delete_char(char_id)
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

	def DELETE_NAME(self, char_name):
		output = {'result' : 'success'}

		i = 0
		for char in self.hpdb.get_chars():
			i += 1
			if char['name'].replace(' ', '').lower() == char_name:
				self.hpdb.delete_char(i)
				return json.dumps(output)

		output['result'] = 'error'
		output['message'] = 'No character named ' + char_name + 'was found.'
		return json.dumps(output)
		
	def PUT_RESET(self):
		output = {'result' : 'success'}
		
		self.hpdb.load_hp_data('characters.json')
		
		return json.dumps(output)

