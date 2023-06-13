import pandas as pd
import random
import requests

# API endpoint URLs


# The application configured to run with docker so the application currently using BASE_URL_DOCKER
# when running the script with python itself use the BASE_URL
BASE_URL = 'http://localhost:8080/api/v1'
BASE_URL_DOCKER = 'http://host.docker.internal:8080/api/v1'
USERS_URL = f'{BASE_URL_DOCKER}/user/registration'
TOPICS_URL = f'{BASE_URL_DOCKER}/topic/addTopic'
POSTS_URL = f'{BASE_URL_DOCKER}/post/addPostToTopic'

# CSV file paths
USERS_CSV = 'reddit_user_data.csv'
TOPICS_CSV = 'reddit_topic_data.csv'
POSTS_CSV = 'reddit_post_data.csv'

# Function to read user data from CSV file
def read_user_data(file_path):
    user_data = pd.read_csv(file_path)
    user_data = user_data.dropna(subset=['name'])
    return user_data

# Function to register a user
def register_user(user_data):
    try:
        formatted_user_data = {
            "firstName": user_data["name"].split()[0],    # Extracting the first name from the "name" field
            "lastName": user_data["name"].split()[1],     # Extracting the last name from the "name" field
            "email": user_data["email"],
            "password": f'{random.randint(100000, 999999)}'  # Generate a random password
        }
        print(f'Sent user data: {formatted_user_data}')
        response = requests.post(USERS_URL, json=formatted_user_data)
        response.raise_for_status()
    except KeyError:
        print(f'Invalid user data: {user_data}')
    except requests.exceptions.RequestException as e:
        print(f'Failed to register user: {user_data["name"]}')
        print(f'Error: {str(e)}')

# Function to read topic data from CSV file
def read_topic_data(file_path):
    topic_data_internal = pd.read_csv(file_path)
    return topic_data_internal

# Function to add a topic
def add_topic(topic_data):
    try:
        formatted_topic_data = {
            "title": topic_data["title"],
            "description": topic_data["description"]
        }
        print(f'Sent topic data: {formatted_topic_data}')
        response = requests.post(TOPICS_URL, json=formatted_topic_data)
        response.raise_for_status()
    except KeyError as e:
        missing_key = e.args[0]
        print(f"Failed to add topic: Missing key '{missing_key}' in topic_data: {topic_data}.")
    except requests.exceptions.RequestException as e:
        print(f'Failed to add topic: {topic_data["title"]}')
        print(f'Error: {str(e)}')

# Function to read post data from CSV file
def read_post_data(file_path):
    post_data = pd.read_csv(file_path)
    return post_data

# Function to add a post to a topic, randomly determining whether to include a location or not.
def add_post_to_topic(post_data, topic_id):
    try:
        if random.choice([True, False]):
            formatted_post_data = {
                "content": post_data["content"],
                "title": post_data["content"]
            }
        else:
            formatted_post_data = {
                "content": post_data["content"],
                "title": post_data["content"],
                "location": {
                    "country": random.choice(["Israel", "UK", "USA", "Japan"]),
                    "city": random.choice(["New York", "London", "Paris", "Tokyo"])
                }
            }
        print(f'Sent post data: {formatted_post_data}')
        url = f'{POSTS_URL}/{topic_id}'
        response = requests.put(url, json=formatted_post_data)
        response.raise_for_status()
    except KeyError:
        print(f"Failed to add post to topic: Missing key in post_data: {post_data}.")
    except requests.exceptions.RequestException as e:
        print(f'Failed to add post to topic: {post_data["content"]}')
        print(f'Error: {str(e)}')

# Read user data from CSV
user_data = read_user_data(USERS_CSV)

# # Read topic data from CSV
topic_data = read_topic_data(TOPICS_CSV)

# # Iterate over the topic data and add users
for _, row in topic_data.iterrows():
    add_topic(row)


# Read post data from CSV
post_data = read_post_data(POSTS_CSV)

# Iterate over the user data and register users
for _, row in user_data.iterrows():
    register_user(row)

# Iterate over the post data and add
for _, row in post_data.iterrows():
    random_topic_id = random.randint(1, 20)
    add_post_to_topic(row, random_topic_id)




