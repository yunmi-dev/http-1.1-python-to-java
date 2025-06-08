from flask import Flask, request
from flask_restx import Resource, Api

app = Flask(__name__)
api = Api(app)

class MembershipHandler():
    # dictionary for membership management
    database = {} 

    # POST request
    def create(self, id, value):
        if id in self.database :
            return {id : "None"}
        else:
            self.database[id] = value
            return {id : self.database[id]}

    # GET request
    def read(self, id):
        if id in self.database:
            return {id : self.database[id]}
        else:
            return {id : "None"}

    # PUT request
    def update(self, id, value):
        if id in self.database :
            self.database[id] = value
            return {id : self.database[id]}
        else:
            return {id : "None"}

    # DELETE request
    def delete(self, id):
        if id in self.database :
            del self.database[id]
            return {id : "Removed"}
        else:
            return {id : "None"}

myManager = MembershipHandler()

@api.route('/membership_api/<string:member_id>')
class MembershipManager(Resource):
    # 'C'reate handler
    def post(self, member_id):
        return myManager.create(member_id, request.form[member_id])
    # 'R'emove handler
    def get(self, member_id):
        return myManager.read(member_id)
    # 'U'pdate handler
    def put(self, member_id):
        return myManager.update(member_id, request.form[member_id])
    # 'D'elete handler
    def delete(seld, member_id):
        return myManager.delete(member_id)

if __name__ == '__main__':
    app.run(debug=True)
    