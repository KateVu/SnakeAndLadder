
/**
 * Building a sheet of buffer image for animation
 * @author Quyen Vu Thi Tu SID 102418320
 * verion 0.1 
 */

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {

    private List<BufferedImage> sprites; //list of sprites in the buffer image
    private int rows; //row
    private int cols; //col

    private BufferedImage spriteSheet; //image contains all sprites
    private int spriteCount; //number of sprite
    private int spriteWidth;//width of sprite
    private int spriteHeight;//heigh of sprite

    /**
     * Constructor
     * @param sheet image contains all sprites
     * @param rows row
     * @param cols col
     * @param count number of sprite
     * @param width width of sprite
     * @param height heigh of sprite
     */
    public SpriteSheet(BufferedImage sheet, int rows, int cols, int count, int width, int height) {
        this.spriteSheet = sheet;
        this.rows = rows;
        this.cols = cols;
        this.spriteCount = count;
        this.spriteWidth = width;
        this.spriteHeight = height;
    	
    	analyseSpriteSheet();
    }
    
    /**
     * build list of sprites and store in an ArrayList
     */
    public void analyseSpriteSheet() {
    	this.sprites = new ArrayList<>(this.spriteCount);
    	
        if (this.spriteWidth == 0) {
            this.spriteWidth = this.spriteSheet.getWidth() / this.cols;
        }
        
        if (this.spriteHeight == 0) {
            this.spriteHeight = this.spriteSheet.getHeight() / this.rows;
        }

        int x = 0;
        int y = 0;
        for (int index = 0; index < this.spriteCount; index++) {
            this.sprites.add(this.spriteSheet.getSubimage(x, y, this.spriteWidth, this.spriteHeight));
            x += this.spriteWidth;
            if (x >= this.spriteWidth * cols) {
                x = 0;
                y += this.spriteHeight;
            }
        }

    }

    /**
     * get a specific sprite frame based on the cycle progress (in a range from 0.0 to 1.0) work in conjunction with SpriteTransition
     * @param progress cycle progress, in a range from 0.0 to 1.0
     * @return sprite specific sprite frame
     */
    public BufferedImage getSprite(double progress) {
        int frame = (int) (this.sprites.size() * progress);
        return this.sprites.get(frame);
    }
    
    /**
     * get a sprite frame at a specific index
     * @param index index of the sprite in the ArrayList
     * @return sprite with specific index in an ArrayList
     */
    public BufferedImage getSpriteByIndex(int index) {
    	if (index < this.sprites.size()) {
    		return this.sprites.get(index);
    	}
    	return null;
    } 

}