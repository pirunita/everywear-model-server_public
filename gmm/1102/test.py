import os
import queue

import os
import queue
import socket
# import tensorflow as tf
import threading
import torch

from GMMutil import GmmModel
from GMMutil import gmm_inference
from GMMutil import load_checkpoint
from flags import get_flags
# from EDMutil import EdmModel
# from EDMutil import edm_inference
# from RMutil import RefinementModel
from mainmodule import data_loader
from mainmodule import select_category

CATEGORY_DICT = {'1001' : 'men_tshirts',
				 '1002' : 'men_long',
				 '1003' : 'men_nambang',
				 '1102' : 'men_pants'
				 }
FLAGS = get_flags()
wait_count = 0
wait_queue = queue.Queue()
soc_queue = queue.Queue()
complete = False
main_socket = socket.socket()


def get_flask():
	global main_socket
	print("Start GET FLASK")
	s = socket.socket()
	host = socket.gethostname()
	port_number = 12223
	s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	s.bind((host, port_number))
	s.setblocking(1)
	s.listen(5)
	c = None
	a = 1
	
	while True:
		try:
			print('[Waiting for connection..]')
			c, addr = s.accept()
			print('Got connection from', addr)

			# Halts
			#time.sleep(3)
			main_socket = c
			print('[Waiting for response...]')
			wait_str = (c.recv(1024)).decode('utf-8')
			print(wait_str)
			print(len(wait_str))

			wait_list = wait_str.split()
			if len(wait_list) < 2:
				print('continue')
				c = None
				a = 1
				continue


			global wait_count,complete

			wait_queue.put(wait_list)
			soc_queue.put(main_socket)
			#wait_count = wait_count + 1

			c = None
			#c, addr = s.accept()
		except Exception as ex:
			print(ex)
	


if __name__ == '__main__':
	os.environ["CUDA_DEVICE_ORDER"] = "PCI_BUS_ID"
	os.environ["CUDA_VISIBLE_DEVICES"] = "0"
	print(FLAGS)
	"""
	
	"""
	
	#graph_men_tshirts_EDM = tf.Graph()
	#graph_men_tshirts_RM = tf.Graph()

	#Loading model in memory
	men_pants_GMM = GmmModel()
	"""
	men_tshirts_EDM = EncoderDecoderModel(category_num='1001', graph2=graph_men_tshirts_EDM)
	men_tshirts_RM = RefinementModel(category_num='1001', graph3=graph_men_tshirts_RM)
	"""
	load_checkpoint(men_pants_GMM, 'model/men_pants/GMM/gmm_final.pth')
	MODEL_DICT = {'1102' : (men_pants_GMM)

				  }
	threading._start_new_thread(get_flask,())
	
	while True:
		try:
			while wait_queue.qsize() != 0:
				"""
				
				"""
				queue_data = wait_queue.get()
				print(queue_data)
				FLAGS.user_id = queue_data[0]
				FLAGS.user_image_id = queue_data[1]
				FLAGS.upper_id = queue_data[2]
				FLAGS.lower_id = queue_data[3]
				FLAGS.is_upper = int(queue_data[4])
				FLAGS.category_num = '1102'
				FLAGS.category_name = select_category('1102')
				FLAGS.input_dir = '../../uploads/users/' + FLAGS.user_id + '/Input/'
				FLAGS.product_dir = '../../uploads/products/'
				FLAGS.stage_dir = '../../uploads/users/' + FLAGS.user_id + '/Output/stage/'
				print(FLAGS)
				print("Load Model")
				gmm_model = MODEL_DICT[FLAGS.category_num]
				"""
				edm_model = MODEL_DICT[FLAGS.category_num][1]
				rm_model = MODEL_DICT[FLAGS.category_num][2]
				try:
					os.mkdir('testdata/' + FLAGS.user_id + '/output')
				except:
					pass
				try:
					os.mkdir('testdata/' + FLAGS.user_id + '/output/stage1')
					os.mkdir('testdata/' + FLAGS.user_id + '/output/stage2')
					os.mkdir('testdata/' + FLAGS.user_id + '/output/final_upper_images')
					os.mkdir('testdata/' + FLAGS.user_id + '/output/final_lower_images')
					os.mkdir('testdata/' + FLAGS.user_id + '/output/final_images')
				except:
					pass

				FLAGS.input_dir = 'testdata/' + FLAGS.user_id + '/input/'
				FLAGS.stage1_dir = 'testdata/' + FLAGS.user_id + '/output/stage1/'
				FLAGS.stage2_dir = 'testdata/' + FLAGS.user_id + '/output/stage2/'
				FLAGS.output_dir = 'testdata/' + FLAGS.user_id + '/output/'

				FLAGS.product_dir = 'data/test_img/' + FLAGS.category_name

				"""
				# Renaming
				user_image_name = FLAGS.user_image_id
				upper_name = FLAGS.upper_id
				lower_name = FLAGS.lower_id
				upper_composed_name = user_image_name + '_' + upper_name + '_'
				lower_composed_name = user_image_name + '_' + lower_name + '_'
				FLAGS.full_composed_name = upper_composed_name + lower_name + '_'
				if FLAGS.is_upper == 0:
					FLAGS.middle_composed_name = lower_composed_name
					product_image_name = lower_name
				elif FLAGS.is_upper == 1:
					FLAGS.middle_composed_name = upper_composed_name
					product_image_name = upper_name

				try:
					# Inference
					#if not os.path.exists(FLAGS.stage2_dir + FLAGS.middle_composed_name + "1_pkl.pkl"):
					image_data, product_image_data = data_loader(FLAGS,
																 user_image_name,
																 product_image_name)
				except Exception as ex:
					print("***********Data Load Fail************")
					print(ex)
				with torch.no_grad():
					gmm_inference(FLAGS, image_data, product_image_data, gmm_model)
				#edm_inference(FLAGS, image_data, product_image_data, edm_model)
				#wait_count = wait_count - 1
				soc = soc_queue.get()
				soc.send("complete".encode('utf-8'))
		except Exception as ex:
			print(ex)


