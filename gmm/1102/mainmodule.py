import os.path as osp
import pickle as pkl
from PIL import Image


def data_loader(FLAGS, user_image_name, product_image_name):
    image_data = {}
    product_image_data = {}
    if FLAGS.is_upper is 0:
        user_image_dir = FLAGS.input_dir + '/body_images/'
        user_segment_dir = FLAGS.input_dir + '/body_segment/'
        user_pose_dir = FLAGS.input_dir + '/body_pose/'
    elif FLAGS.is_upper is 1:
        user_image_dir = FLAGS.input_dir + 'upper_images/'
        user_segment_dir = FLAGS.input_dir + '/upper_segment/'
        user_pose_dir = FLAGS.input_dir + '/upper_pose/'

    product_image_dir = FLAGS.product_dir

    image = Image.open(osp.join(user_image_dir, user_image_name + '.png'))
    """ 
    with open(user_segment_dir, user_image_name + '.pkl') as f:
        try:
            segment_map  = pkl.load(f, encoding='latin-1')
        except EOFError:
            pass
    
    with open(user_pose_dir, user_image_name + '.pkl') as f:
        try:
            pose_raw = pkl.load(f, encoding='latin1')
        except EOFError:
            pass
    """
    with open(user_pose_dir + user_image_name + '.pkl', 'rb') as f:
        try:
            pose_raw = pkl.load(f, encoding='latin-1')
        except EOFError:
            pass

    with open(user_segment_dir + user_image_name + '.pkl', 'rb') as f:
        try:
            segment_map = pkl.load(f, encoding='latin-1')
        except EOFError:
            pass

    image_data['image'] = image
    image_data['pose_raw'] = pose_raw
    image_data['segment_map'] = segment_map

    product_image_data['prod_image'] = Image.open(
        osp.join(product_image_dir, product_image_name + '.png'))

    return image_data, product_image_data


def select_category(category_num):
    return {'1001': 'men_tshirts',
            '1002': 'men_nambang',
            '1003': 'men_long',
            '1101': 'men_pants'}.get(category_num, "No data")
