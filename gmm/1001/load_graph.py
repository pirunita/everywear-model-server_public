import os
import tensorflow as tf

def load_graph(frozen_graph_filename):
    with tf.gfile.GFile(frozen_graph_filename, 'rb') as f:
        graph_def = tf.GraphDef()
        graph_def.ParseFromString(f.read())
        return graph_def

tf.import_graph_def(load_graph('frozen_inference_graph.pb'))


'''
그래프만 pb의 파일로 저장하는 형태
'''
# Get the current directory
dir_path = os.path.dirname(os.path.realpath(__file__))
print("Current directory : ", dir_path)
save_dir = dir_path + '/Protobufs'

graph = tf.get_default_graph()

# Create a session for running Ops on the Graph.
sess = tf.Session()

print("Restoring the model to the default graph ...")
saver = tf.train.import_meta_graph(dir_path + '/model.ckpt.meta')
saver.restore(sess,tf.train.latest_checkpoint(dir_path))
print("Restoring Done .. ")

print("Saving the model to Protobuf format: ", save_dir)

#Save the model to protobuf  (pb and pbtxt) file.
tf.train.write_graph(sess.graph_def, save_dir, "Binary_Protobuf.pb", False)
tf.train.write_graph(sess.graph_def, save_dir, "Text_Protobuf.pbtxt", True)
print("Saving Done .. ")