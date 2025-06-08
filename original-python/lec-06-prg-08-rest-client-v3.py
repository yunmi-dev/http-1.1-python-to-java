import requests

# Reads a non registered member : error-case
r = requests.get('http://127.0.0.1:5000/membership_api/0001')
print("#1 Code:", r.status_code, ">>", "JSON:", r.json(), ">>", "JSON Result:", r.json()['0001'])

# Creates a new registered member : non-error case
r = requests.post('http://127.0.0.1:5000/membership_api/0001', data={'0001':'apple'})
print("#2 Code:", r.status_code, ">>", "JSON:", r.json(), ">>", "JSON Result:", r.json()['0001'])

# Reads a registered member : non-error case
r = requests.get('http://127.0.0.1:5000/membership_api/0001')
print("#3 Code:", r.status_code, ">>", "JSON:", r.json(), ">>", "JSON Result:", r.json()['0001'])

# Creates an already registered member : error case
r = requests.post('http://127.0.0.1:5000/membership_api/0001', data={'0001':'xpple'})
print("#4 Code:", r.status_code, ">>", "JSON:", r.json(), ">>", "JSON Result:", r.json()['0001'])

# Updates a non registered member : error case
r = requests.put('http://127.0.0.1:5000/membership_api/0002', data={'0002':'xrange'})
print("#5 Code:", r.status_code, ">>", "JSON:", r.json(), ">>", "JSON Result:", r.json()['0002'])

# Updates a registered member : non-error case
r = requests.post('http://127.0.0.1:5000/membership_api/0002', data={'0002':'xrange'})
r = requests.put('http://127.0.0.1:5000/membership_api/0002', data={'0002':'orange'})
print("#6 Code:", r.status_code, ">>", "JSON:", r.json(), ">>", "JSON Result:", r.json()['0002'])

# Delete a registered member : non-error case
r = requests.delete('http://127.0.0.1:5000/membership_api/0001')
print("#7 Code:", r.status_code, ">>", "JSON:", r.json(), ">>", "JSON Result:", r.json()['0001'])

# Delete a non registered member : non-error case
r = requests.delete('http://127.0.0.1:5000/membership_api/0001')
print("#8 Code:", r.status_code, ">>", "JSON:", r.json(), ">>", "JSON Result:", r.json()['0001'])
